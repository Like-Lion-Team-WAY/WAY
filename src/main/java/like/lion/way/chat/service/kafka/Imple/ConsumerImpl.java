package like.lion.way.chat.service.kafka.Imple;

import com.fasterxml.jackson.databind.ObjectMapper;
import like.lion.way.chat.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsumerImpl {

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;

//    @Value("${server.port}")
//    private String serverPort;

//    @KafkaListener(topics = "topic-messages", groupId = "chat-group-#{@environment.getProperty('server.port')}")
    @KafkaListener(topics = "topic-messages", groupId = "chat-group")
    public void listen(String message) {
        try {
            // JSON 문자열을 Message 객체로 변환
            Message messageObject = objectMapper.readValue(message, Message.class);
            System.out.println("Received message: " + messageObject.getText());

            messagingTemplate.convertAndSend("/topic/messages/" + messageObject.getChatId(), messageObject);
        } catch (Exception e) {
            e.printStackTrace(); // JSON 역직렬화 오류 처리
        }
    }
}