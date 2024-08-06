package like.lion.way.feed.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
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

    //username 추가해줘야 됨
    @GetMapping("/questions/create/{userId}")
    public String createQuestion(Model model, HttpServletRequest request, @PathVariable("userId") Long userId) {
        // JWT 토큰에서 사용자 이름 추출
        String token = jwtUtil.getCookieValue(request, "accessToken");
        Long loginId = jwtUtil.getUserIdFromToken(token);
        //얘는 로그인 유저
        User loginUser= userService.findByUserId(loginId);
        model.addAttribute("loginUser", loginUser);

        // 얘는 질문 페이지 소유자의 유저 정보
        User user = userService.findByUserId(userId);
        model.addAttribute("user", user);
        model.addAttribute("question", questionService.getQuestionByUser(user));

        return "pages/feed/questionPage";
    }

    @PostMapping("/questions/create")
    public String createQuestion(
            @RequestParam("question") String question,
            @RequestParam("isAnonymous") boolean isAnonymous,
            @RequestParam(value = "image", required = false) MultipartFile image,
            HttpServletRequest request) {

        // JWT 토큰에서 사용자 이름 추출
        String token = jwtUtil.getCookieValue(request, "accessToken");
        Long userId = jwtUtil.getUserIdFromToken(token);

        // 사용자 정보 조회
        User user = userService.findByUserId(userId);
        log.info("question: {}", question);
        Question newQuestion = new Question();
        newQuestion.setQuestion(question);  //질문 저장
        newQuestion.setQuestionDate(LocalDateTime.now()); //질문 생성일
        //익명 여부에 따라
        if(isAnonymous) {
            newQuestion.setQuestioner(null);

        } else {
            newQuestion.setQuestioner(user);
        }
        if (!image.isEmpty()) { //이미지
            try {
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                String filePath = uploadDir + File.separator + fileName;
                File dest = new File(filePath);
                image.transferTo(dest);
                newQuestion.setQuestionImageUrl(fileName); // 웹에서 접근할 경로
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        questionService.saveQuestion(newQuestion);
//        newQuestion.setAnswer(null);
//        newQuestion.setAnswerDate(null);
//        newQuestion.setQuestionDeleteYN(false);
//        newQuestion.setQuestionStatus(false);
//        newQuestion.setQuestionPinStatus(false);
//        newQuestion.setQuestionRejected(false);
        return "redirect:/questions/create";
    }

    @PostMapping("/questions/answer/{questionId}")
    public String answerQuestion(@RequestParam("answer") String answer, @PathVariable("questionId") Long questionId) {
        Question question = questionService.getQuestionById(questionId);
        question.setAnswer(answer);
        question.setAnswerDate(LocalDateTime.now());
        questionService.saveQuestion(question);
        return "redirect:/questions/create";
    }
}
