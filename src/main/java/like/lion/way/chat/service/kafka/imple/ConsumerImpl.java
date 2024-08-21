package like.lion.way.chat.service.kafka.imple;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import like.lion.way.chat.domain.Message;
import like.lion.way.chat.domain.dto.ReceiveMessageDTO;
import like.lion.way.chat.repository.MessageRepository;
import like.lion.way.chat.service.kafka.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsumerImpl implements Consumer {

    private static final Map<Long, Set<Long>> enterUser = new HashMap<>();
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;

//    @Value("${server.port}")
//    private String serverPort;

    @Override
//    @KafkaListener(topics = "topic-messages", groupId = "chat-group-#{@environment.getProperty('server.port')}")
    @KafkaListener(topics = "topic-messages", groupId = "chat-group")
    public void listen(String message) {
        try {
            // JSON 문자열을 Message 객체로 변환
            ReceiveMessageDTO receiveMessageDTO = objectMapper.readValue(message, ReceiveMessageDTO.class);
            String type = receiveMessageDTO.getType();
            Long chatId = receiveMessageDTO.getChatId();
            Long senderId = receiveMessageDTO.getSenderId();
            Set<Long> chatIds = enterUser.get(chatId);

            if (type.equals("open")) {
                enterProcessing(chatIds, chatId, senderId);

            } else if (type.equals("close")) {
                leaveProcessing(chatIds, chatId, senderId);

            } else if (!type.startsWith("create") && !type.equals("delete") &&
                    chatIds != null && chatIds.contains(receiveMessageDTO.getReceiverId())) {
                readProcessing(receiveMessageDTO);
            }
            messagingTemplate.convertAndSend("/topic/messages/" + receiveMessageDTO.getChatId(), receiveMessageDTO);

        } catch (Exception e) {
            e.printStackTrace(); // JSON 역직렬화 오류 처리
        }
    }

    private void enterProcessing(Set<Long> chatIds, Long chatId, Long senderId) {
        if (chatIds == null) {
            enterUser.put(chatId, new HashSet<>());
        }
        enterUser.get(chatId).add(senderId);
    }

    private void leaveProcessing(Set<Long> chatIds, Long chatId, Long senderId) {
        chatIds.remove(senderId);
        if (chatIds.isEmpty()) {
            enterUser.remove(chatId);
        }
    }

    private void readProcessing(ReceiveMessageDTO receiveMessageDTO) {
        Message messageFromDB = messageRepository.findById(receiveMessageDTO.getId()).get();
        messageFromDB.setIsRead(true);
        messageRepository.save(messageFromDB);

        receiveMessageDTO.setIsRead(true);
    }
}