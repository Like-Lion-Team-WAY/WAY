package like.lion.way.feed.controller;

import like.lion.way.feed.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class Question2Controller {
    private final QuestionService questionService;
    @PostMapping("/questions/pin/{questionId}")
    public String pinQuestion(@PathVariable("questionId") Long questionId) {
        questionService.pinQuestion(questionId);
        return "redirect:/questions/create";
    }
}
