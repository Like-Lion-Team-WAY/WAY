package like.lion.way.chat.service;

import like.lion.way.chat.domain.Chat;
import like.lion.way.chat.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {
    Message findLastByChatId(Long id);
    Page<Message> findAllByChatId(Long chatId, Pageable pageable);
    Page<Message> findAllByChatIdAndIdLessThan(Long chatId, String lastLoadMessageId, Pageable pageable);
    void createStartMessage(Chat newChat);
}
