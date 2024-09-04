package like.lion.way.feed.service.imp;

import java.time.LocalDateTime;
import like.lion.way.alarm.domain.AlarmType;
import like.lion.way.alarm.event.AlarmEvent;
import like.lion.way.feed.domain.PostComment;
import like.lion.way.feed.domain.dto.PostCommentDto;
import like.lion.way.feed.repository.PostCommentRepository;
import like.lion.way.feed.service.PostCommentService;
import like.lion.way.feed.service.PostService;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostCommentServiceImpl implements PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostService postService;
    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    /**
     * 댓글 조회
     * @param commentId
     */
    public PostComment getCommentById(Long commentId) {
        return postCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID"));
    }

    /**
     * 댓글 저장
     * @param postId
     * @param postCommentDto
     * @param userId
     */
    @Override
    @Transactional
    public PostComment saveComment(Long postId, PostCommentDto postCommentDto, Long userId) {
        PostComment postComment = new PostComment();
        postComment.setPostCommentContent(postCommentDto.getPostCommentContent());
        postComment.setPostCommentCreatedAt(LocalDateTime.now());
        postComment.setPost(postService.getPostById(postId));
        postComment.setUser(userService.findByUserId(userId));
        var value = postCommentRepository.save(postComment);

        // 트랜잭션 종료 후 이벤트 발생
        User fromUser = value.getUser();
        User toUser = value.getPost().getUser();
        if (fromUser.equals(toUser)) {
            return value;
        }
        String urlParam = value.getPost().getPostId().toString();

        AlarmEvent event = new AlarmEvent(this, AlarmType.COMMENT, fromUser, toUser,
                urlParam);
        publisher.publishEvent(event);

        return value;
    }

    /**
     * 댓글 수정
     * @param commentId
     * @param content
     */
    @Override
    @Transactional
    public PostComment updateComment(Long commentId, String content) {
        PostComment postComment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID"));
        postComment.setPostCommentContent(content);
        return postCommentRepository.save(postComment);
    }

    /**
     * 댓글 삭제
     * @param commentId
     */
    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        postCommentRepository.deleteById(commentId);
    }

    /**
     * 대댓글 저장
     * @param postId
     * @param userId
     * @param postCommentContent
     * @param parentCommentPreCommentId
     */
    @Override
    @Transactional
    public PostComment savePreComment(Long postId, Long userId, String postCommentContent,
                                      Long parentCommentPreCommentId) {
        PostComment postComment = new PostComment();
        postComment.setPostCommentContent(postCommentContent);
        postComment.setPostCommentCreatedAt(LocalDateTime.now());
        postComment.setPost(postService.getPostById(postId));
        postComment.setUser(userService.findByUserId(userId));
        postComment.setPostCommentPreCommentId(parentCommentPreCommentId);
        var value = postCommentRepository.save(postComment);

        // 트랜잭션 종료 후 이벤트 발생
        var ParentComment = postCommentRepository.findById(parentCommentPreCommentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID"));
        User fromUser = value.getUser();
        User toUser = ParentComment.getUser();
        if (fromUser.equals(toUser)) {
            return value;
        }
        String urlParam = value.getPost().getPostId().toString();

        AlarmEvent event = new AlarmEvent(this, AlarmType.REPLY, fromUser, toUser,
                urlParam);
        publisher.publishEvent(event);

        return value;
    }
}
