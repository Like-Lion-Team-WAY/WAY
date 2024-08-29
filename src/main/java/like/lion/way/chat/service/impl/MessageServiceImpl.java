package like.lion.way.chat.service.impl;

import static like.lion.way.chat.constant.ChatMessageType.CREATE;
import static like.lion.way.chat.constant.ChatMessageType.WITHDRAWAL;
import static like.lion.way.chat.constant.OpenNicknameState.NICKNAME_OPEN_STATE;

import java.time.LocalDateTime;
import java.util.List;
import like.lion.way.alarm.event.ChatAlarmEvent;
import like.lion.way.chat.domain.Chat;
import like.lion.way.chat.domain.Message;
import like.lion.way.chat.repository.MessageRepository;
import like.lion.way.chat.service.MessageService;
import like.lion.way.chat.service.kafka.Producer;
import like.lion.way.user.domain.User;
import like.lion.way.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final Producer producer;
    private final ApplicationEventPublisher publisher;

    @Override
    public Message findLastByChatId(Long id) {
        return messageRepository.findFirstByChatIdOrderByCreatedAtDesc(id);
    }

    @Override
    public Page<Message> findAllByChatId(Long chatId, Pageable pageable) {
        return messageRepository.findAllByChatId(chatId, pageable);
    }

    @Override
    public Page<Message> findAllByChatIdAndIdLessThan(Long chatId, String lastLoadMessageId, Pageable pageable) {
        return messageRepository.findAllByChatIdAndIdLessThan(chatId, lastLoadMessageId, pageable);
    }

    @Override
    public void createStartMessage(Chat chat) {
        User chatMaker = chat.getAnswerer();

        Message message = new Message();
        message.setChatId(chat.getId());
        message.setSenderId(chatMaker.getUserId());
        message.setReceiverId(chat.getQuestionerId());
        message.setText("[" + chatMaker.getNickname() + "] 님이 채팅을 시작했습니다");
        message.setType(CREATE.get() + chat.getId());
        message.setCreatedAt(LocalDateTime.now());

        messageRepository.save(message);

        message.setChatId(0L);
        producer.sendMessage(message);
        ChatAlarmEvent event = new ChatAlarmEvent(
                this, userRepository.findById(message.getReceiverId()).orElse(null), 1L);
        publisher.publishEvent(event);
    }

    @Override
    public void createWithdrawalMessage(Chat chat, Long userId, String result) {
        String nickname = getNickname(chat, userId);
        String text = "[" + nickname + "] 님이 나가겼습니다.";
        Message message = new Message();
        message.setChatId(chat.getId());
        message.setText(text);
        message.setSenderId(userId);
        message.setType(result + WITHDRAWAL.get());
        producer.sendMessage(message);
    }

    @Override
    @Async
    public void readMessage(Long userId, Long chatId) {
        List<Message> messages;
        if ((messages = messageRepository.findByChatIdAndReceiverIdAndIsReadFalse(chatId, userId)) != null) {
            for (Message message : messages) {
                message.setIsRead(true);
            }
            messageRepository.saveAll(messages);
        }
        ChatAlarmEvent event = new ChatAlarmEvent(this, userRepository.findById(userId).orElse(null),
                messages.size() * -1L);
        publisher.publishEvent(event);
    }

    @Override
    public Message findById(String id) {
        return messageRepository.findById(id).orElse(null);
    }

    private String getNickname(Chat chat, Long userId) {
        if (chat.isAnswerer(userId)) {
            return chat.getAnswererNickname();
        } else {
            return chat.getQuestionerNickname(chat.getNicknameOpen() != NICKNAME_OPEN_STATE.get());
        }
    }
}
