package like.lion.way.feed.service;

import like.lion.way.feed.domain.PostComment;
import like.lion.way.feed.domain.dto.PostCommentDto;
import org.springframework.stereotype.Service;

@Service
public interface PostCommentService {
    PostComment getCommentById(Long commentId);

    PostComment saveComment(Long postId, PostCommentDto postCommentDto, Long userId);

    PostComment updateComment(Long commentId, String content);

    void deleteComment(Long commentId);

    PostComment savePreComment(Long postId, Long userId, String postCommentContent, Long parentCommentPreCommentId);
}
