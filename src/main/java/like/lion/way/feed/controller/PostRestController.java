package like.lion.way.feed.controller;

import like.lion.way.feed.domain.Post;
import like.lion.way.feed.service.PostService;
import like.lion.way.file.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;
    private final S3Service s3Service;

    /**
     * 게시글 수정
     * @param postId
     * @param title
     * @param content
     */
    @PatchMapping("/posts/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable("postId") Long postId,
                                             @RequestParam("title") String title,
                                             @RequestParam("content") String content) {

        try {
            postService.updatePost(postId, title, content);
            return ResponseEntity.ok("게시글 수정 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 수정 실패");
        }
    }

    /**
     * 게시글 삭제
     * @param id
     */
    @DeleteMapping("/posts")
    public ResponseEntity<String> deletePost(@RequestParam("id") Long id) {

        try {
            Post post = postService.getPostById(id);
            if (post.getPostImageUrl() != null) {
                s3Service.deleteFile(post.getPostImageUrl());
            }
            postService.deletePost(id);
            return ResponseEntity.ok("게시글 삭제 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 삭제 실패");
        }
    }
}
