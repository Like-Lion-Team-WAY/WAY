package like.lion.way.feed.controller;

import like.lion.way.feed.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;

    @PatchMapping("/posts/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable("postId") Long postId, @RequestParam String title, @RequestParam String content) {
        try {
            postService.updatePost(postId, title, content);
            return ResponseEntity.ok("Post updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update post.");
        }
    }

    @DeleteMapping("/posts")
    public ResponseEntity<String> deletePost(@RequestParam Long id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.ok("Post deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete post.");
        }
    }
}
