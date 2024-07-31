package like.lion.way.alarm.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AlarmEvent extends ApplicationEvent {
    private String message;
    private String url;

    public AlarmEvent(Object source, String message, String url) {
        super(source);
        this.message = message;
        this.url = url;
    }
}
