package like.lion.way.alarm.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import like.lion.way.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "alarms")
@NoArgsConstructor // for JPA
@Getter
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "alarm_message", nullable = false)
    private String message;

    @Column(name = "alarm_url", nullable = false)
    private String url;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    public Alarm(User user, String message, String url) {
        this.user = user;
        this.message = message;
        this.url = url;
        this.createdAt = Timestamp.valueOf(LocalDateTime.now());
    }
}
