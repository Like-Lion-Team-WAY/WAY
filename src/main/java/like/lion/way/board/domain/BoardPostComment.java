package like.lion.way.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(name = "board_post_comment_user_id", nullable = false)
    private Long userId;

    @Builder
    private BoardPostComment(BoardPost boardPost, String content, Long preCommentId, Long userId) {

        this.boardPost = boardPost;
        this.content = content;
        this.preCommentId = preCommentId;
        this.userId = userId;

    }

}
