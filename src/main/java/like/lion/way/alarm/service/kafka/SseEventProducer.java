package like.lion.way.alarm.service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SseEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public SseEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 카프카로 이벤트를 전송
     * @param userId SSE 데이터를 전송 받을 대상자 userId
     * @param eventName 이벤트 이름 (subscribe, count, chat, alarm)
     * @param jsonData 전송할 데이터
     */
    public void sendEvent(Long userId, String eventName, String jsonData) {
        String message = userId + ":" + eventName + ":" + jsonData;
        kafkaTemplate.send("topic-sse", message);
    }
}
