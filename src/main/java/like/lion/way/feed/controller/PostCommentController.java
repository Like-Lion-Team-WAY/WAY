package like.lion.way.feed.controller;

import like.lion.way.feed.domain.dto.PostCommentDto;
import like.lion.way.feed.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;

    @PostMapping("/posts/comments/{postId}")
    public String saveComments(@PathVariable("postId") Long postId, PostCommentDto postCommentDto) {
        postCommentService.saveComment(postId, postCommentDto);
        return "redirect:/posts/detail/" + postId;
    }
}
