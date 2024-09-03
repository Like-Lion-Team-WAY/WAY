package like.lion.way.alarm.service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SseEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public SseEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(Long userId, String eventName, String jsonData) {
        String message = userId + ":" + eventName + ":" + jsonData;
        kafkaTemplate.send("topic-sse", message);
    }
}
