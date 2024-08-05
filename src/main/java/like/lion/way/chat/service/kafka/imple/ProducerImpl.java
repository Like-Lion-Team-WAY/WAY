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
            messageRepository.save(message);

            Chat chat = chatRepository.findById(message.getChatId()).get();

            String nickname;
            if (message.getUserId().equals(chat.getUser1().getUserId())) {
                nickname = chat.getUser1().getNickname();
            } else if (message.getUserId().equals(chat.getUser2().getUserId()) && chat.isNicknameOpen2()) {
                nickname = chat.getUser2().getNickname();
            } else {
                nickname = "익명";
            }

            ReceiveMessageDTO receiveMessageDTO = new ReceiveMessageDTO(message, nickname);

            // ReceiveMessageDTO 객체를 JSON 문자열로 변환
            String receiveMessageDTOAsString = objectMapper.writeValueAsString(receiveMessageDTO);
            kafkaTemplate.send(TOPIC, receiveMessageDTOAsString);

        } catch (JsonProcessingException e) {
            e.printStackTrace(); // JSON 변환 오류 처리
        }
    }
}