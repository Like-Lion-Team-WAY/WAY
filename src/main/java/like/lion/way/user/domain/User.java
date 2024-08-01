package like.lion.way.user.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Set;
import like.lion.way.feed.domain.Like;
import like.lion.way.feed.domain.Post;
import like.lion.way.feed.domain.PostComment;
import like.lion.way.feed.domain.QuestionBox;
import like.lion.way.feed.domain.Question;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(unique = true, name = "username" , nullable = false)
    private String username;

    @Column(nullable = false , name = "nickname")
    private String nickname;

    @Column(nullable = false,name = "provider")
    private String provider;

    @Column(nullable = false,name = "provider_id")
    private String providerId;

    @Column(name = "created_at")
    private LocalDate createdAt;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Post> posts;

//    @OneToMany(mappedBy = "user")
//    private Set<PostComment> comments;
//
//    @OneToMany(mappedBy = "questioner", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Question> questionsAsked;
//
//    @OneToMany(mappedBy = "answerer", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Question> questionsAnswered;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<QuestionBox> questionBoxes;
//
//    @OneToMany(mappedBy = "user")
//    private Set<Like> likes;

}
