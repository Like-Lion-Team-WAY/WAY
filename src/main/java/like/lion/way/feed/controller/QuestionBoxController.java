package like.lion.way.feed.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class QuestionBoxController {
    @PostMapping("/questions/archieve")
    public String archieveQuestion() {
        return null;
    }
}
