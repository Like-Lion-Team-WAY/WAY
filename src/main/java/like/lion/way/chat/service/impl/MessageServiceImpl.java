package like.lion.way.chat.service.impl;

import like.lion.way.chat.domain.Message;
import like.lion.way.chat.repository.MessageRepository;
import like.lion.way.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    final private MessageRepository messageRepository;

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
}
