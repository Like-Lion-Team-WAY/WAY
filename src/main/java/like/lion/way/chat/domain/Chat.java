package like.lion.way.chat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import like.lion.way.feed.domain.Question;
import like.lion.way.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "chats")
@Getter
@Setter
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user_id2", nullable = false)
    private User user2;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "chat_nickname_open", nullable = false)
    private boolean nicknameOpen1 = true;

    @Column(name = "chat_nickname_open2", nullable = false)
    private boolean nicknameOpen2 = false;

    @Column(name = "chat_user_active", nullable = false)
    private boolean userActive1 = true;

    @Column(name = "chat_user_active2", nullable = false)
    private boolean userActive2 = true;

    @Column(name = "chat_name", nullable = false)
    private String name;

    @Column(name = "chat_created_at", nullable = false)
    private LocalDateTime createdAt;

    public boolean isAccessibleUser(Long userId) {
        return (userId.equals(getUser1().getUserId()) && isUserActive1()) ||
                (userId.equals(getUser2().getUserId()) && isUserActive2());
    }

    public boolean isUser1(Long userId) {
        return userId.equals(getUser1().getUserId());
    }

    public boolean isUser2(Long userId) {
        return userId.equals(getUser2().getUserId());
    }

    public boolean isActive() {
        return isUserActive1() && isUserActive2();
    }

    public boolean userExist() {
        return userActive1 || userActive2;
    }
}
