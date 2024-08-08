package like.lion.way.feed.controller;

import java.util.List;
import like.lion.way.feed.domain.PostBox;
import like.lion.way.feed.domain.QuestionBox;
import like.lion.way.feed.service.PostBoxService;
import like.lion.way.feed.service.QuestionBoxService;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ArchieveController {
    private final PostBoxService postBoxService;
    private final QuestionBoxService questionBoxService;
    private final UserService userService;
    @GetMapping("/all/archieve/{userId}")
    public String archieveAll(@PathVariable("userId") Long userId, Model model) {
        User user= userService.findByUserId(userId);
        List<PostBox> posts= postBoxService.getPostBoxByUserId(user);
        model.addAttribute("posts", posts);
        List<QuestionBox> questions= questionBoxService.getQuestionBoxByUserId(user);
        model.addAttribute("questions", questions);
        return "pages/feed/archievePage";
    }


}
