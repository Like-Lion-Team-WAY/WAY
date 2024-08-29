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
@Table(name = "board_posts")
public class BoardPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(name = "board_post_title", nullable = false)
    private String title;

    @Column(name = "board_post_content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    @Column(name = "board_post_created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "board_post_anonymous_permission")
    private boolean anonymousPermission;

    @Builder
    public BoardPost(User user, Board board, String title, String content, boolean anonymousPermission) {

        this.user = user;
        this.board = board;
        this.title = title;
        this.content = content;
        this.anonymousPermission = anonymousPermission;

    }

    public void editBoardPost(String title, String content) {

        this.title = title;
        this.content = content;

    }

}
