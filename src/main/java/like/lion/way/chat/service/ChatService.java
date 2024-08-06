package like.lion.way.chat.service;

import like.lion.way.chat.domain.Chat;
import like.lion.way.feed.domain.Question;
import like.lion.way.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ChatService {
    Chat findById(Long chatId);
    Page<Chat> findUserChatList(User user, Pageable pageable);
    Chat findByQuestion(Question question);
    Chat createChat(Question question);
    String userLeave(Chat chat, Long userId);
}
