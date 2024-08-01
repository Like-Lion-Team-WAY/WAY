package like.lion.way.feed.controller;

import java.time.LocalDateTime;
import like.lion.way.feed.domain.PostComment;
import like.lion.way.feed.domain.dto.PostCommentDto;
import like.lion.way.feed.service.PostCommentService;
import like.lion.way.feed.service.PostService;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;
    private final PostService postService;
    private final UserService userService;

    @PostMapping("/posts/comments/{postId}")
    public String saveComments(@PathVariable("postId") Long postId, PostCommentDto postCommentDto) {
        PostComment postComment= new PostComment();
        postComment.setPostCommentContent(postCommentDto.getPostCommentContent());
        postComment.setPostCommentCreatedAt(LocalDateTime.now());
        postComment.setPost(postService.getPostById(postId));
        postCommentService.saveComment(postComment);
        return "redirect:/posts/detail/" + postId;
    }
}
