package like.lion.way.alarm.event;

import like.lion.way.user.domain.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AlarmEvent extends ApplicationEvent {
    private User user;
    private String message;
    private String url;

    public AlarmEvent(Object source, User user, String message, String url) {
        super(source);
        this.user = user;
        this.message = message;
        this.url = url;
    }
}
