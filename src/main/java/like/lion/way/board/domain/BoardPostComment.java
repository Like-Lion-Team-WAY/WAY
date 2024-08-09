package like.lion.way.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import like.lion.way.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board_post_comments")
public class BoardPostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_post_comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_post_id")
    private BoardPost boardPost;

    @Column(name = "board_post_comment_content", nullable = false)
    private String content;

    @CreationTimestamp
    @Column(name = "board_post_comment_created_at")
    private LocalDateTime createdAt;

    @Column(name = "board_post_comment_pre_comment_id")
    private Long preCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_post_comment_user_id")
    private User user;

    @Column(name = "board_post_comment_anonymous_permission")
    private boolean anonymousPermission;

    @Builder
    private BoardPostComment(BoardPost boardPost, String content, Long preCommentId, User user, boolean anonymousPermission) {

        this.boardPost = boardPost;
        this.content = content;
        this.preCommentId = preCommentId;
        this.user = user;
        this.anonymousPermission = anonymousPermission;

    }

}
