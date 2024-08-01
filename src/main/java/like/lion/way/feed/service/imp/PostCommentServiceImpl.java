package like.lion.way.feed.service.imp;

import like.lion.way.feed.domain.PostComment;
import like.lion.way.feed.repository.PostCommentRepository;
import like.lion.way.feed.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostCommentServiceImpl implements PostCommentService {

    private final PostCommentRepository postCommentRepository;

    @Override
    @Transactional
    public PostComment saveComment(PostComment postComment) {
        return postCommentRepository.save(postComment);
    }

    @Override
    public PostComment updateComment(Long commentId, String content) {
        PostComment postComment = postCommentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("Invalid comment ID"));
        postComment.setPostCommentContent(content);
        return postCommentRepository.save(postComment);
    }

    @Override
    public void deleteComment(Long commentId) {
        postCommentRepository.deleteById(commentId);
    }
}
