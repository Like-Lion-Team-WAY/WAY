package like.lion.way.feed.controller;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import like.lion.way.feed.domain.Question;
import like.lion.way.feed.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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

    @Value("${image.upload.dir}")
    private String uploadDir;

    //username 추가해줘야 됨
    @GetMapping("/questions/create")
    public String createQuestion(Model model) {
        model.addAttribute("question", questionService.getAllQuestions());
        return "pages/feed/questionPage";
    }

    @PostMapping("/questions/create")
    public String createQuestion(
            @RequestParam("question") String question,
            @RequestParam("isAnonymous") boolean isAnonymous,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Principal principal) {

        String username = isAnonymous ? "익명" : (principal != null ? principal.getName() : "unknown");
        log.info("question: {}", question);
        log.info("username: {}", username);
        Question newQuestion = new Question();
        newQuestion.setQuestion(question);  //질문 저장
        newQuestion.setQuestionDate(LocalDateTime.now()); //질문 생성일
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
//        newQuestion.setQuestioner(null); // username 추가해줘야 됨
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
