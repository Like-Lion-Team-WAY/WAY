package like.lion.way.alarm.service;

import like.lion.way.alarm.domain.Alarm;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AlarmSseEmitters {
    /**
     * SseEmitter 추가 (SSE 구독)
     */
    SseEmitter add(Long userId);

    /**
     * 알림 전송
     */
    void send(Alarm alarm);
}
