package like.lion.way.feed.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;
import like.lion.way.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "posts")
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(name = "post_title", nullable = false)
    private String postTitle;

    @Column(name = "post_content", nullable = false)
    private String postContent;

    @Column(name = "post_created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime postCreatedAt;

    @Column(name = "post_delete_status", columnDefinition = "TINYINT DEFAULT 0")
    private boolean postDeleteStatus;

    @Column(name = "post_image_url")
    private String postImageUrl;

    @Column(name = "post_pin_status", columnDefinition = "TINYINT DEFAULT 0")
    private boolean postPinStatus;

    @Column(name = "post_like", columnDefinition = "INT DEFAULT 0")
    private Integer postLike = 0;
    //nullable=false 였던거 지움
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private Set<Like> likes;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private Set<PostComment> postComments;
}
