package like.lion.way.alarm.service;

import like.lion.way.alarm.domain.Alarm;
import like.lion.way.user.domain.User;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AlarmSseEmitters {
    SseEmitter add(Long userId, String windowId);
    void sendSubscriptions(Long userId);
    void sendAlarmCount(Long userId);
    void sendChatCount(Long userId);
    void sendAlarm(Long userId, Alarm alarm);
    void send(Long userId, String name, String jsonData);
}
