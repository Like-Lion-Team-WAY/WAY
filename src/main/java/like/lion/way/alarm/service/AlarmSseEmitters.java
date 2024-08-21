package like.lion.way.alarm.service;

import like.lion.way.alarm.domain.Alarm;
import like.lion.way.user.domain.User;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AlarmSseEmitters {
    /**
     * SseEmitter 추가 (SSE 구독)
     */
    SseEmitter add(Long userId, String windowId);

    /**
     * 알림 전송
     */
    void sendAlarmCount(Long userId);
    void sendAlarm(Long userId, Alarm alarm);

    /**
     * 채팅 알림 전송
     */
    void sendChatCount(Long userId, Long count);
}
