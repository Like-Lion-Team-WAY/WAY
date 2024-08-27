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
import lombok.ToString;

@Entity
@Table(name = "chats")
@Getter
@Setter
@ToString
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "answerer_id")
    private User answerer;

    @ManyToOne
    @JoinColumn(name = "questioner_id")
    private User questioner;

    @ManyToOne
    @JoinColumn(name = "question_id")
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
        return (answerer != null && userId.equals(answerer.getUserId()) && answererActive) ||
                (questioner != null && userId.equals(questioner.getUserId()) && questionerActive);
    }

    public boolean isAnswerer(Long userId) {
        return answerer != null && userId.equals(answerer.getUserId());
    }

    public boolean isQuestioner(Long userId) {
        return questioner != null && userId.equals(questioner.getUserId());
    }

    public boolean isActive() {
        return answererActive && questionerActive;
    }

    public boolean userExist() {
        return answererActive || questionerActive;
    }

    public Long getQuestionerId() {
        return questioner != null ? questioner.getUserId() : null;
    }

    public Long getAnswererId() {
        return answerer != null ? answerer.getUserId() : null;
    }

    public String getAnswererNickname() {
        return answerer != null ? answerer.getNickname() : "탈퇴한 유저입니다";
    }

    public String getQuestionerNickname(boolean isOpen) {
        return questioner != null ? questioner.getNickname(isOpen) : "탈퇴한 유저입니다";
    }

    public boolean withNonMember() {
        return questioner == null || answerer == null;
    }
}
