package like.lion.way.chat.service.kafka.imple;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import like.lion.way.chat.domain.Message;
import like.lion.way.chat.repository.MessageRepository;
import like.lion.way.chat.service.kafka.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProducerImpl implements Producer {
    private static final String TOPIC = "topic-messages";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper; // JSON 변환을 위한 ObjectMapper
    private final MessageRepository messageRepository;

    @Override
    public void sendMessage(Message message) {
        try {
            message.setCreatedAt(LocalDateTime.now());
            messageRepository.save(message);

            // Message 객체를 JSON 문자열로 변환
            System.out.println("[producer] sending message: " + message.getText());
            String messageAsString = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(TOPIC, messageAsString);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // JSON 변환 오류 처리
        }
    }
}