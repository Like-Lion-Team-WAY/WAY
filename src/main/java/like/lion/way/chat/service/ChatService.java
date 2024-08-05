package like.lion.way.chat.service;

import like.lion.way.chat.domain.Chat;
import like.lion.way.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ChatService {
    Chat findById(Long chatId);
    Page<Chat> findAllByUser1OrUser2(User user, Pageable pageable);
}
