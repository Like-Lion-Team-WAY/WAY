package like.lion.way.alarm.event;
import like.lion.way.alarm.domain.AlarmType;
import like.lion.way.user.domain.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AlarmEvent extends ApplicationEvent {
    private final AlarmType type;
    private final User fromUser;
    private final User toUser;
    private final String previewContent;
    private final String pathVariable; // post_id 등 경로 설정 시 필요한 변수

    public AlarmEvent(Object source, AlarmType type, User fromUser, User toUser, String messageContent, String pathVariable) {
        super(source);
        this.type = type;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.previewContent = messageContent;
        this.pathVariable = pathVariable;
    }
}