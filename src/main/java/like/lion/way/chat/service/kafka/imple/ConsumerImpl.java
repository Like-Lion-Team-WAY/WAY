package like.lion.way.chat.service.kafka.imple;

import static like.lion.way.chat.constant.ChatMessageType.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import like.lion.way.alarm.event.ChatAlarmEvent;
import like.lion.way.chat.domain.Message;
import like.lion.way.chat.domain.dto.ReceiveMessageDTO;
import like.lion.way.chat.repository.MessageRepository;
import like.lion.way.chat.service.kafka.Consumer;
import like.lion.way.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
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
    private final UserRepository userRepository;
    private final ApplicationEventPublisher publisher;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Override
    @KafkaListener(topics = "topic-messages")
    public void listen(String message) {
        try {
            // JSON 문자열을 Message 객체로 변환
            ReceiveMessageDTO receiveMessageDTO = objectMapper.readValue(message, ReceiveMessageDTO.class);
            String type = receiveMessageDTO.getType();
            Long chatId = receiveMessageDTO.getChatId();
            Long senderId = receiveMessageDTO.getSenderId();
            Set<Long> chatIds = enterUser.get(chatId);

            if (type.equals(OPEN.get())) {
                enterProcessing(chatIds, chatId, senderId);

            } else if (type.equals(CLOSE.get())) {
                leaveProcessing(chatIds, chatId, senderId);

            } else if (!type.startsWith(CREATE.get()) && !type.startsWith(DELETE.get()) && chatIds != null) {
                if (chatIds.contains(receiveMessageDTO.getReceiverId())) {
                    readProcessing(receiveMessageDTO);
                } else if (groupId.equals("group1")) {
                    ChatAlarmEvent event = new ChatAlarmEvent(
                            this, userRepository.findById(receiveMessageDTO.getReceiverId()).orElse(null), 1L);
                    publisher.publishEvent(event);
                }
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
        if (groupId.equals("group1")) {
            Message messageFromDB = messageRepository.findById(receiveMessageDTO.getId()).get();
            messageFromDB.setIsRead(true);
            messageRepository.save(messageFromDB);
        }

        receiveMessageDTO.setIsRead(true);
    }
}
