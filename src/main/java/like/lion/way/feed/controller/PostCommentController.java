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

    //피드의 댓글 저장
    @PostMapping("/posts/comments/{postId}")
    public String saveComments(@PathVariable("postId") Long postId,
                               @RequestParam("userId") Long userId,
                               PostCommentDto postCommentDto) {

        postCommentService.saveComment(postId, postCommentDto, userId);
        return "redirect:/posts/detail/" + postId;
    }

    //피드의 대댓글 저장
    @PostMapping("/posts/comments/pre/{postId}")
    public String savePreComments(@PathVariable("postId") Long postId,
                                  @RequestParam("userId") Long userId,
                                  @RequestParam("postCommentContent") String postCommentContent,
                                  @RequestParam("parentCommentPreCommentId") Long parentCommentPreCommentId) {

        postCommentService.savePreComment(postId, userId, postCommentContent, parentCommentPreCommentId);
        return "redirect:/posts/detail/" + postId;
    }

}
