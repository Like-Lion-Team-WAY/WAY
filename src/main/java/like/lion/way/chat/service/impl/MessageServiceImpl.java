package like.lion.way.chat.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import like.lion.way.chat.domain.Chat;
import like.lion.way.chat.domain.Message;
import like.lion.way.chat.repository.MessageRepository;
import like.lion.way.chat.service.MessageService;
import like.lion.way.chat.service.kafka.Producer;
import like.lion.way.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final Producer producer;

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
        message.setReceiverId(chat.getQuestioner().getUserId());
        message.setText("[" + chatMaker.getNickname() + "] 님이 채팅을 시작했습니다");
        message.setType("create" + chat.getId());
        message.setCreatedAt(LocalDateTime.now());

        messageRepository.save(message);

        message.setChatId(0L);
        producer.sendMessage(message);
    }

    @Override
    public void readMessage(Long userId, Long chatId) {
        List<Message> messages;
        if ((messages = messageRepository.findByChatIdAndReceiverIdAndIsReadFalse(chatId, userId)) != null) {
            for (Message message : messages) {
                message.setIsRead(true);
            }
            messageRepository.saveAll(messages);
        }
    }
}
