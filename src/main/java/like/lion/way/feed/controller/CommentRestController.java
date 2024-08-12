package like.lion.way.feed.controller;

import java.util.Map;
import like.lion.way.feed.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentRestController {
    private final PostCommentService postCommentService;
    //댓글 수정
    @PatchMapping("/posts/comments/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable("commentId") Long commentId, @RequestBody Map<String, String> payload) {
        try {
            String updatedContent = payload.get("updatedContent");
            postCommentService.updateComment(commentId, updatedContent);
            return ResponseEntity.ok("comment updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update comment.");
        }
    }
    //댓글 삭제
    @DeleteMapping("/posts/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") Long commentId) {
        try {
            postCommentService.deleteComment(commentId);
            return ResponseEntity.ok("comment deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete comment.");
        }
    }

}
