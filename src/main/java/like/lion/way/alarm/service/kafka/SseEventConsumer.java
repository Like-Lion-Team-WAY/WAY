package like.lion.way.alarm.service.kafka;

import like.lion.way.alarm.service.AlarmSseEmitters;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SseEventConsumer {
    private final AlarmSseEmitters alarmSseEmitters;

    public SseEventConsumer(AlarmSseEmitters alarmSseEmitters) {
        this.alarmSseEmitters = alarmSseEmitters;
    }

    /**
     * 카프카로부터 이벤트를 수신해서 SSE로 데이터 전송
     * @param message userId:eventName:jsonData
     *                userId: SSE 데이터를 전송 받을 대상자 userId
     *                eventName: 이벤트 이름 (subscribe, count, chat, alarm)
     *                jsonData: 전송할 데이터
     */
    @KafkaListener(topics = "topic-sse")
    public void listen(String message) {
        // 메시지 형식: userId:eventName:jsonData
        String[] parts = message.split(":", 3);
        Long userId = Long.parseLong(parts[0]);
        String eventName = parts[1];
        String jsonData = parts[2];

        alarmSseEmitters.send(userId, eventName, jsonData);
    }
}
