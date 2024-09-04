package like.lion.way.alarm.service;

import like.lion.way.alarm.domain.Alarm;
import like.lion.way.alarm.domain.AlarmType;
import like.lion.way.alarm.dto.AlarmMessageDto;
import like.lion.way.alarm.event.AlarmEvent;
import like.lion.way.user.domain.User;
import org.springframework.data.domain.Page;
import java.util.Map;

public interface AlarmService {
    boolean isAlarmEnabled(User user, AlarmType type);
    Alarm createAlarm(AlarmEvent alarmEvent);
    void saveAlarm(Alarm alarm);
    Long countAlarm(User user);
    Long countAlarm(Long userId);
    Page<AlarmMessageDto> getAlarm(Long userId, int page, int size);
    void deleteAlarm(Long alarmId);
    void deleteAllAlarms(Long userId);
    void updateAlarmSetting(Long userId, AlarmType type, boolean enabled);
    Map<String, Boolean> getAlarmSetting(Long userId);
}
