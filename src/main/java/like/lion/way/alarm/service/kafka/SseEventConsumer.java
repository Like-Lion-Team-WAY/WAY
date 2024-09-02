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
    
    @KafkaListener(topics = "topic-sse", groupId = "sse-group-#{@environment.getProperty('server.port')}")
    public void listen(String message) {
        // 메시지 형식: userId:eventName:jsonData
        String[] parts = message.split(":", 3);
        Long userId = Long.parseLong(parts[0]);
        String eventName = parts[1];
        String jsonData = parts[2];

        alarmSseEmitters.send(userId, eventName, jsonData);
    }
}
