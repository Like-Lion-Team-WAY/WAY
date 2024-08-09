package like.lion.way.feed.controller;

import like.lion.way.feed.domain.dto.PostCommentDto;
import like.lion.way.feed.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;

    @PostMapping("/posts/comments/{postId}")
    public String saveComments(@PathVariable("postId") Long postId,@RequestParam("userId") Long userId, PostCommentDto postCommentDto) {
        postCommentService.saveComment(postId, postCommentDto, userId);
        return "redirect:/posts/detail/" + postId;
    }
    @PostMapping("/posts/comments/pre/{postId}")
    public String savePreComments(@PathVariable Long postId, @RequestParam Long userId, @RequestParam String postCommentContent, @RequestParam(required = false) Long parentCommentPreCommentId){
        postCommentService.savePreComment(postId, userId, postCommentContent, parentCommentPreCommentId);
        return "redirect:/posts/detail/" + postId;
    }

}
