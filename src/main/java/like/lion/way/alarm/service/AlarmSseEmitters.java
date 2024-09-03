package like.lion.way.alarm.service;

import like.lion.way.alarm.domain.Alarm;
import like.lion.way.user.domain.User;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AlarmSseEmitters {
    // 구독 신청
    SseEmitter add(Long userId, String windowId);
    // 구독 후 첫 데이터 (알람 개수, 채팅 개수) 전송
    void sendSubscriptions(Long userId);
    // 알람 개수 전송 (헤더)
    void sendAlarmCount(Long userId);
    // 채팅 개수 전송 (헤더)
    void sendChatCount(Long userId);
    // 새로운 알람 전송
    void sendAlarm(Long userId, Alarm alarm);
    // Kafka에서 수신한 메시지를 클라이언트에게 전송
    void send(Long userId, String name, String jsonData);
}
