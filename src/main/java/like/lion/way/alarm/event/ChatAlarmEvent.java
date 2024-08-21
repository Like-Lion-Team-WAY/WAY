package like.lion.way.alarm.event;

import like.lion.way.user.domain.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChatAlarmEvent extends ApplicationEvent {
    private final User user;
    private final Long gap;

    public ChatAlarmEvent(Object source, User user, Long gap) {
        super(source);
        this.user = user;
        this.gap = gap;
    }
}
