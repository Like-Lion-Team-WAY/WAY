package like.lion.way.chat.service;

import java.util.List;
import like.lion.way.chat.domain.Chat;
import like.lion.way.feed.domain.Question;
import like.lion.way.user.domain.User;
import org.springframework.stereotype.Service;

@Service
public interface ChatService {
    Chat findById(Long chatId);
    List<Chat> findUserChatList(User user);
    Chat findByQuestion(Question question);
    Chat createChat(Question question);
    String userLeave(Chat chat, Long userId);
    void changeName(Chat chat, String name);
}
