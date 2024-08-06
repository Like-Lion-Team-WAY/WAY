package like.lion.way.chat.service.kafka.imple;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import like.lion.way.chat.domain.Chat;
import like.lion.way.chat.domain.Message;
import like.lion.way.chat.domain.dto.ReceiveMessageDTO;
import like.lion.way.chat.repository.ChatRepository;
import like.lion.way.chat.repository.MessageRepository;
import like.lion.way.chat.service.kafka.Producer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProducerImpl implements Producer {
    private static final String TOPIC = "topic-messages";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper; // JSON 변환을 위한 ObjectMapper
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    @Override
    public void sendMessage(Message message) {

        try {
            message.setCreatedAt(LocalDateTime.now());

            ReceiveMessageDTO receiveMessageDTO = null;
            if (message.getType().equals("delete")) {
                message.setReceiverId(0L);
                receiveMessageDTO = new ReceiveMessageDTO(message, null, null);

            } else if (message.getType().startsWith("create")) {
                Long newChatId = Long.valueOf(message.getType().substring(6));
                Chat chat = chatRepository.findById(newChatId).orElse(null);
                receiveMessageDTO = new ReceiveMessageDTO(message, chat.getName(), null);

            } else {
                Chat chat = chatRepository.findById(message.getChatId()).orElse(null);
                if (chat.isUser1(message.getSenderId())) {
                    message.setReceiverId(chat.getUser2().getUserId());
                } else {
                    message.setReceiverId(chat.getUser1().getUserId());
                }

                messageRepository.save(message);

                String nickname;
                if (chat.isUser1(message.getSenderId())) {
                    nickname = chat.getUser1().getNickname();
                } else {
                    nickname = chat.getUser2().getNickname(!chat.isNicknameOpen2());
                }
                receiveMessageDTO = new ReceiveMessageDTO(message, chat.getName(), nickname);
            }

            // ReceiveMessageDTO 객체를 JSON 문자열로 변환
            String receiveMessageDTOAsString = objectMapper.writeValueAsString(receiveMessageDTO);
            kafkaTemplate.send(TOPIC, receiveMessageDTOAsString);

        } catch (JsonProcessingException e) {
            e.printStackTrace(); // JSON 변환 오류 처리
        }
    }
}