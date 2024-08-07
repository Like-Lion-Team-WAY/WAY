package like.lion.way.feed.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class QuestionBoxController {
    @PostMapping("/questions/archieve")
    public String archieveQuestion(@RequestParam("questionId") Long questionId, @RequestParam("userId") Long userId) {

        return null;
    }
}
