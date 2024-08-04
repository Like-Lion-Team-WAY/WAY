package like.lion.way.alarm.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import like.lion.way.user.domain.User;

@Entity
@Table(name = "alarm_settings")
@NoArgsConstructor // for JPA
@Getter
public class AlarmSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_setting_id", nullable = false)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "alarm_setting_new_question")
    private boolean newQuestion;

    @Column(name = "alarm_setting_reply")
    private boolean reply;

    @Column(name = "alarm_setting_comment")
    private boolean comment;

    @Column(name = "alarm_setting_answer")
    private boolean answer;

    @Column(name = "alarm_setting_board_comment")
    private boolean boardComment;
}
