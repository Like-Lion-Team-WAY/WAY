package like.lion.way.chat.service.impl;

import java.util.List;
import like.lion.way.chat.domain.Message;
import like.lion.way.chat.repository.MessageRepository;
import like.lion.way.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    final private MessageRepository messageRepository;

    @Override
    public List<Message> findAllMessageByChatId(Long chatId) {
        return messageRepository.findAllByChatId(chatId);
    }
}
