package like.lion.way.alarm.dto;

import like.lion.way.alarm.domain.Alarm;
import lombok.Getter;

@Getter
public class AlarmMessageDto {
    private Long id;
    private String message;
    private String url;

    public AlarmMessageDto(Alarm alarm) {
        this.id = alarm.getId();
        this.message = alarm.getMessage();
        this.url = alarm.getUrl();
    }
}
