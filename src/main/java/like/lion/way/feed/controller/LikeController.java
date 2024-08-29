package like.lion.way.feed.controller;

import like.lion.way.feed.domain.Question;
import like.lion.way.feed.service.LikeService;
import like.lion.way.feed.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LikeController {
    private final LikeService likeService;
    private final QuestionService questionService;

    // 게시글에 대한 좋아요
    @PostMapping("/posts/like")
    public String likePost(@RequestParam("postId") Long postId,
                           @RequestParam("userId") Long userId) {
        
        likeService.likePost(postId, userId);
        return "redirect:/posts/detail/" + postId;
    }

    //질문에 대한 좋아요
    @PostMapping("/questions/like")
    public String likeQuestion(@RequestParam("questionId") Long questionId,
                               @RequestParam("userId") Long userId) {

        likeService.likeQuestion(questionId, userId);
        Question question = questionService.getQuestionById(questionId);
        Long answererId = question.getAnswerer().getUserId();
        return "redirect:/questions/create/" + answererId;
    }
}
