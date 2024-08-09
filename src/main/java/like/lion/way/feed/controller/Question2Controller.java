package like.lion.way.feed.controller;

import like.lion.way.feed.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class Question2Controller {
    private final QuestionService questionService;
    //질문 고정
    @PostMapping("/questions/pin/{questionId}")
    public String pinQuestion(@PathVariable("questionId") Long questionId) {
        questionService.pinQuestion(questionId);
        return "redirect:/questions/create";
    }
    //질문 삭제
    @PostMapping("/questions/delete")
    public String deleteQuestion(@RequestParam("questionId") Long questionId) {
        questionService.deleteQuestion(questionId);
        return "redirect:/questions/create";
    }
}
