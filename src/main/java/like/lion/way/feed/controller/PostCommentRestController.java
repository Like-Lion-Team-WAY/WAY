package like.lion.way.feed.controller;

import java.util.Map;
import like.lion.way.feed.domain.dto.PostCommentDto;
import like.lion.way.feed.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostCommentRestController {
    private final PostCommentService postCommentService;

    //피드의 댓글 저장
    @PostMapping("/posts/comments/{postId}")
    public ResponseEntity<String> saveComments(@PathVariable("postId") Long postId,
                               @RequestParam("userId") Long userId,
                               PostCommentDto postCommentDto) {

        try{
            postCommentService.saveComment(postId, postCommentDto, userId);
            return ResponseEntity.ok("댓글 저장 성공");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 저장 실패");
        }
    }

    //피드의 대댓글 저장
    @PostMapping("/posts/comments/pre/{postId}")
    public ResponseEntity<String> savePreComments(@PathVariable("postId") Long postId,
                                  @RequestParam("userId") Long userId,
                                  @RequestParam("postCommentContent") String postCommentContent,
                                  @RequestParam("parentCommentPreCommentId") Long parentCommentPreCommentId) {

        try {
            postCommentService.savePreComment(postId, userId, postCommentContent, parentCommentPreCommentId);
            return ResponseEntity.ok("대댓글 저장 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("대댓글 저장 실패");
        }
    }

    //댓글 수정
    @PatchMapping("/posts/comments/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable("commentId") Long commentId,
                                                @RequestBody Map<String, String> payload) {
        try {
            String updatedContent = payload.get("updatedContent");
            postCommentService.updateComment(commentId, updatedContent);
            return ResponseEntity.ok("댓글 수정 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 수정 실패");
        }
    }

    //댓글 삭제
    @DeleteMapping("/posts/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") Long commentId) {
        try {
            postCommentService.deleteComment(commentId);
            return ResponseEntity.ok("댓글 삭제 실패");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 삭제 실패");
        }
    }


}