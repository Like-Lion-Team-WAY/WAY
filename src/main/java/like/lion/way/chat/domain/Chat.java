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
    @JoinColumn(name = "answerer_id", nullable = false)
    private User answerer;

    @ManyToOne
    @JoinColumn(name = "questioner_id", nullable = false)
    private User questioner;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "chat_nickname_open", nullable = false)
    private Integer nicknameOpen = 0;

    @Column(name = "chat_answerer_active", nullable = false)
    private boolean answererActive = true;

    @Column(name = "chat_questioner_active", nullable = false)
    private boolean questionerActive = true;

    @Column(name = "chat_name", nullable = false)
    private String name;

    @Column(name = "chat_created_at", nullable = false)
    private LocalDateTime createdAt;

    public boolean isAccessibleUser(Long userId) {
        return (userId.equals(answerer.getUserId()) && answererActive) ||
                (userId.equals(questioner.getUserId()) && questionerActive);
    }

    public boolean isAnswerer(Long userId) {
        return userId.equals(answerer.getUserId());
    }

    public boolean isQuestioner(Long userId) {
        return userId.equals(questioner.getUserId());
    }

    public boolean isActive() {
        return answererActive && questionerActive;
    }

    public boolean userExist() {
        return answererActive || questionerActive;
    }
}
