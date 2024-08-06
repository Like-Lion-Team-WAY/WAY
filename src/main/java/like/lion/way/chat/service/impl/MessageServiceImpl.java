package like.lion.way.chat.service.impl;

import java.time.LocalDateTime;
import like.lion.way.chat.domain.Chat;
import like.lion.way.chat.domain.Message;
import like.lion.way.chat.repository.MessageRepository;
import like.lion.way.chat.service.MessageService;
import like.lion.way.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

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
        User chatMaker = chat.getUser1();

        Message message = new Message();
        message.setChatId(chat.getId());
        message.setUserId(chatMaker.getUserId());
        message.setText("[" + chatMaker.getNickname() + "] 님이 채팅을 시작했습니다");
        message.setType("start");
        message.setCreatedAt(LocalDateTime.now());

        messageRepository.save(message);
    }
}
