package like.lion.way.alarm.event;
import like.lion.way.alarm.domain.AlarmType;
import like.lion.way.user.domain.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AlarmEvent extends ApplicationEvent {
    private AlarmType type;
    private User user;

    public AlarmEvent(Object source, AlarmType type, User user) {
        super(source);
        this.type = type;
        this.user = user;
    }
}