package like.lion.way.alarm.dto;

import like.lion.way.alarm.domain.AlarmType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlarmRequestDto {
    private AlarmType alarmType;
    private boolean enabled;
}
