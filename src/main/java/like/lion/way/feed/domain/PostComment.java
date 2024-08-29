package like.lion.way.feed.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import like.lion.way.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post_comment")
@Setter
@Getter
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postCommentId;

    @Column(name = "post_comment_content", nullable = false)
    private String postCommentContent;

    @Column(name = "post_comment_created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime postCommentCreatedAt;

    @Column(name = "post_comment_pre_comment_id")
    private Long postCommentPreCommentId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
