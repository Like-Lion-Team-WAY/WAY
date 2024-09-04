package like.lion.way.chat.controller;

import like.lion.way.chat.domain.Message;
import like.lion.way.chat.service.kafka.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/**
 * 유저의 메세지 받아오기 위한 웹소켓 controller
 *
 * @author Lee NaYeon
 */
@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final Producer producer;

    /**
     * 유저로 부터 받은 메세지를 kafka-producer로 전달
     *
     * @param message 유저로 부터 받은 메세지
     */
    @MessageMapping("/sendMessage")
    public void sendMessage(Message message) {
        producer.sendMessage(message);
    }
}