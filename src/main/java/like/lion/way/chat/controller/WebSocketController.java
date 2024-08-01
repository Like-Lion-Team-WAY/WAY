package like.lion.way.chat.controller;

import like.lion.way.chat.domain.Message;
import like.lion.way.chat.service.kafka.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final Producer kafkaProducer;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/sendMessage")
    public void sendMessage(Message message) {
        System.out.println("[socket] Sending message: " + message.getText());
        kafkaProducer.sendMessage(message); // JSON 변환 후 Kafka로 전송
    }
}