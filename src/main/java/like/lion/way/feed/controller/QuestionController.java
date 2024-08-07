package like.lion.way.feed.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import like.lion.way.feed.domain.Question;
import like.lion.way.feed.service.QuestionService;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@Slf4j
public class QuestionController {

    private final QuestionService questionService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Value("${image.upload.dir}")
    private String uploadDir;

    // 로그인한 사용자 정보 조회
    private User getLoginUser(HttpServletRequest request) {
        String token = jwtUtil.getCookieValue(request, "accessToken");
        Long loginId = jwtUtil.getUserIdFromToken(token);
        return userService.findByUserId(loginId);
    }

    // 공통된 질문에서의 코드 중복 제거
    private void setCommonModelAttributes(Model model, User user) {
        model.addAttribute("user", user);
        List<Question> questions = questionService.getQuestionByAnswerer(user).stream()
                .filter(q -> !q.getQuestionRejected())
                .collect(Collectors.toList());
        model.addAttribute("questions", questions);
    }

    // 내 질문 창으로 이동
    @GetMapping("/questions/create")
    public String createMyQuestion(Model model, HttpServletRequest request) {
        User loginUser = getLoginUser(request);
        model.addAttribute("loginUser", loginUser);
        setCommonModelAttributes(model, loginUser);
        return "pages/feed/questionPage";
    }

    // userId에 해당하는 질문 페이지로 이동
    @GetMapping("/questions/create/{userId}")
    public String createQuestion(Model model, HttpServletRequest request, @PathVariable("userId") Long userId) {
        User loginUser = getLoginUser(request);
        model.addAttribute("loginUser", loginUser);
        User user = userService.findByUserId(userId);
        setCommonModelAttributes(model, user);
        return "pages/feed/questionPage";
    }

    // 질문 등록
    @PostMapping("/questions/create/{userId}")
    public String createQuestion(
            @PathVariable("userId") Long userId,
            @RequestParam("question") String questionText,
            @RequestParam("isAnonymous") boolean isAnonymous,
            @RequestParam(value = "image", required = false) MultipartFile image,
            HttpServletRequest request) {

        User user = getLoginUser(request);

        Question newQuestion = new Question();
        newQuestion.setQuestion(questionText);
        newQuestion.setQuestionDate(LocalDateTime.now());

        if (isAnonymous) {
            newQuestion.setQuestioner(null);
        } else {
            newQuestion.setQuestioner(user);
        }

        if (image != null && !image.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                String filePath = uploadDir + File.separator + fileName;
                File dest = new File(filePath);
                image.transferTo(dest);
                newQuestion.setQuestionImageUrl(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        User questionPageUser = userService.findByUserId(userId);
        newQuestion.setAnswerer(questionPageUser);
        newQuestion.setQuestionDeleteYN(false);
        newQuestion.setQuestionStatus(false);
        newQuestion.setQuestionPinStatus(false);
        newQuestion.setQuestionRejected(false);
        questionService.saveQuestion(newQuestion);

        return "redirect:/questions/create/" + userId;
    }

    // 질문 답변
    @PostMapping("/questions/answer/{questionId}")
    public String answerQuestion(@RequestParam("answer") String answer, @PathVariable("questionId") Long questionId) {
        Question question = questionService.getQuestionById(questionId);
        question.setAnswer(answer);
        question.setQuestionStatus(true);
        question.setAnswerDate(LocalDateTime.now());
        questionService.saveQuestion(question);
        return "redirect:/questions/create";
    }

    // 거절된 질문 등록
    @PostMapping("/questions/enroll/rejected")
    public String enrollRejected(@RequestParam("questionId") Long questionId) {
        Question question = questionService.getQuestionById(questionId);
        question.setQuestionRejected(true);
        questionService.saveQuestion(question);
        return "redirect:/questions/create";
    }
}
