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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ArchieveController {
    private final PostBoxService postBoxService;
    private final QuestionBoxService questionBoxService;
    private final UserService userService;

    /**
     * 전체 보관함 페이지
     * @param userId
     * @param model
     */
    @GetMapping("/all/archieve/{userId}")
    public String archieveAll(@PathVariable("userId") Long userId,
                              Model model) {

        User user = userService.findByUserId(userId);
        List<PostBox> posts = postBoxService.getPostBoxByUserId(user);
        model.addAttribute("posts", posts);
        List<QuestionBox> questions = questionBoxService.getQuestionBoxByUserId(user);
        model.addAttribute("questions", questions);
        model.addAttribute("user", user);
        return "pages/feed/archievePage";
    }

    /**
     * 질문 보관
     * @param questionId
     * @param userId
     */
    @PostMapping("/questions/archieve")
    public String archieveQuestion(@RequestParam("questionId") Long questionId,
                                   @RequestParam("userId") Long userId) {

        questionBoxService.archieveQuestion(questionId, userId);
        return "redirect:/posts";
    }

    /**
     * 게시글 보관
     * @param postId
     * @param userId
     */
    @PostMapping("/posts/archieve/{postId}")
    public String archievePost(@PathVariable("postId") Long postId,
                               @RequestParam("userId") Long userId) {

        postBoxService.archievePost(postId, userId);
        return "redirect:/posts/detail/" + postId;
    }

}
