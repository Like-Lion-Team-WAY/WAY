package like.lion.way.alarm.domain;

import jakarta.persistence.CascadeType;
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
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "alarm_settings")
@NoArgsConstructor // for JPA
@Getter
@Setter
public class AlarmSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_setting_id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
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

    @Column(name = "alarm_setting_board_reply")
    private boolean boardReply;

    // User와의 관계를 설정하는 생성자
    public AlarmSetting(User user) {
        this.user = user;
        newQuestion = true;
        reply = true;
        comment = true;
        answer = true;
        boardComment = true;
        boardReply = true;
    }
}
