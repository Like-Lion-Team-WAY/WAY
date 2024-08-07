package like.lion.way.feed.service.imp;

import java.time.LocalDateTime;
import like.lion.way.feed.domain.PostComment;
import like.lion.way.feed.domain.dto.PostCommentDto;
import like.lion.way.feed.repository.PostCommentRepository;
import like.lion.way.feed.service.PostCommentService;
import like.lion.way.feed.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostCommentServiceImpl implements PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostService postService;

    @Override
    @Transactional
    public PostComment saveComment(Long postId, PostCommentDto postCommentDto) {
        PostComment postComment = new PostComment();
        postComment.setPostCommentContent(postCommentDto.getPostCommentContent());
        postComment.setPostCommentCreatedAt(LocalDateTime.now());
        postComment.setPost(postService.getPostById(postId));
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
