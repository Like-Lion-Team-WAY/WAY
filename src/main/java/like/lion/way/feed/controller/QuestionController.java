package like.lion.way.feed.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Comparator;
import like.lion.way.feed.domain.Question;
import like.lion.way.feed.service.QuestionService;
import like.lion.way.feed.util.UserUtil;
import like.lion.way.file.service.S3Service;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class QuestionController {

    private final QuestionService questionService;
    private final UserUtil userUtil;
    private final UserService userService;
    private final S3Service s3Service;

    /**
     * 질문 페이지
     * @param model
     * @param request
     */
    @GetMapping("/questions/create")
    public String createMyQuestion(Model model,
                                   HttpServletRequest request) {

        User loginUser = userUtil.getLoginUser(request);
        return loadQuestionPage(model, request, loginUser != null ? loginUser.getUserId() : null);
    }

    /**
     * 질문 페이지 (다른 사용자)
     * @param model
     * @param request
     * @param userId
     */
    @GetMapping("/questions/create/{userId}")
    public String createQuestion(Model model,
                                 HttpServletRequest request,
                                 @PathVariable("userId") Long userId) {

        return loadQuestionPage(model, request, userId);
    }

    /**
     * 질문 작성
     * @param userId
     * @param question
     * @param isAnonymous
     * @param image
     * @param request
     */
    @PostMapping("/questions/create/{userId}")
    public String createQuestion(@PathVariable("userId") Long userId,
                                 @RequestParam("question") String question,
                                 @RequestParam("isAnonymous") boolean isAnonymous,
                                 @RequestParam(value = "image", required = false) MultipartFile image,
                                 HttpServletRequest request) {

        User loginUser = userUtil.getLoginUser(request);
        String imageUrl = (image != null && !image.isEmpty()) ? s3Service.uploadFile(image) : null;

        if (loginUser != null) {
            // 로그인
            questionService.saveQuestion(loginUser, userId, question, isAnonymous, imageUrl, request);
        } else {
            // 비로그인
            questionService.saveQuestion(userId, question, imageUrl, request);
        }

        return "redirect:/questions/create/" + userId;
    }

    /**
     * 질문 답변
     * @param answer
     * @param questionId
     */
    @PostMapping("/questions/answer/{questionId}")
    public String answerQuestion(@RequestParam("answer") String answer,
                                 @PathVariable("questionId") Long questionId) {

        Question question = questionService.getQuestionById(questionId);
        questionService.saveQuestion(question, answer);
        return "redirect:/questions/create";
    }

    /**
     * 거절 질문 등록
     * @param questionId
     */
    @PostMapping("/questions/enroll/rejected")
    public String enrollRejected(@RequestParam("questionId") Long questionId) {

        Question question = questionService.getQuestionById(questionId);
        questionService.rejectedQuestion(question);
        return "redirect:/questions/rejected";
    }

    /**
     * 질문 고정 (핀)
     * @param questionId
     */
    @PostMapping("/questions/pin/{questionId}")
    public String pinQuestion(@PathVariable("questionId") Long questionId) {

        questionService.pinQuestion(questionId);
        return "redirect:/questions/create";
    }

    /**
     * 질문 삭제
     * @param questionId
     */
    @PostMapping("/questions/delete")
    public String deleteQuestion(@RequestParam("questionId") Long questionId) {

        Question question = questionService.getQuestionById(questionId);
        if (question.getQuestionImageUrl() != null) {
            s3Service.deleteFile(question.getQuestionImageUrl());
        }
        questionService.deleteQuestion(questionId);
        return "redirect:/questions/create";
    }

    /**
     * 공통적으로 질문 페이지에 띄우는 데이터들
     * @param model
     * @param request
     * @param userId
     */
    private String loadQuestionPage(Model model,
                                    HttpServletRequest request,
                                    Long userId) {

        User loginUser = userUtil.getLoginUser(request);
        model.addAttribute("loginUser", loginUser);

        if (userId == null) {
            log.error("userId is null");
            return "redirect:/posts";
        }

        User user = userService.findByUserId(userId);
        model.addAttribute("user", user);

        List<Question> questions = questionService.getQuestionByAnswerer(user, request);
        model.addAttribute("question", filterNonPinnedQuestions(questions));
        model.addAttribute("pinQuestion", filterPinnedQuestions(questions));

        return "pages/feed/questionPage";
    }

    /**
     * 고정되지 않은 질문 필터링
     * @param questions
     */
    private List<Question> filterNonPinnedQuestions(List<Question> questions) {
        return questions.stream()
                .filter(q -> !q.getQuestionRejected() && !q.getQuestionPinStatus())
                .sorted(Comparator.comparing(Question::getQuestionDate))
                .toList();
    }

    /**
     * 고정된 질문 필터링
     * @param questions
     * @return
     */
    private List<Question> filterPinnedQuestions(List<Question> questions) {
        return questions.stream().filter(q -> !q.getQuestionRejected() && q.getQuestionPinStatus())
                .sorted(Comparator.comparing(Question::getQuestionDate))
                .toList();
    }
}
