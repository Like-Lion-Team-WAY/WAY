package like.lion.way.feed.controller;

import like.lion.way.feed.service.QuestionBoxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
@Slf4j
public class QuestionBoxController {
    private final QuestionBoxService questionBoxService;
    //질문 보관
    @PostMapping("/questions/archieve")
    public String archieveQuestion(@RequestParam("questionId") Long questionId, @RequestParam("userId") Long userId) {
        questionBoxService.archieveQuestion(questionId, userId);
        return "redirect:/posts";
    }
}
