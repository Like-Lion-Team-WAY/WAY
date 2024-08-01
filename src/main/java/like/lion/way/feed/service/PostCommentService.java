package like.lion.way.feed.service;

import like.lion.way.feed.domain.PostComment;
import org.springframework.stereotype.Service;

@Service
public interface PostCommentService {
    PostComment saveComment(PostComment postComment);

    PostComment updateComment(Long commentId, String content);

    void deleteComment(Long commentId);
}
