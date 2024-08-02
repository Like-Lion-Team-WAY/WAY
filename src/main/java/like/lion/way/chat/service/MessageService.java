package like.lion.way.chat.service;

import java.util.List;
import like.lion.way.chat.domain.Message;

public interface MessageService {
    List<Message> findAllMessageByChatId(Long chatId);
}
