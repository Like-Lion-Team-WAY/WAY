package like.lion.way.alarm.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import like.lion.way.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_alarms")
@NoArgsConstructor
@Getter
public class ChatAlarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_alarm_id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "chat_alarm_user_id", nullable = false)
    private User user;

    @Column(name = "chat_alarm_count", nullable = false)
    private Long count;

    public ChatAlarm(User user) {
        this.user = user;
        this.count = 0L;
    }

    public void count(Long count) {
        this.count += count;
        if (this.count < 0) {
            this.count = 0L;
        }
    }
}