package like.lion.way.chat.service.impl;


import java.time.LocalDateTime;
import java.util.List;
import like.lion.way.chat.domain.Chat;
import like.lion.way.chat.repository.ChatRepository;
import like.lion.way.chat.repository.MessageRepository;
import like.lion.way.chat.service.ChatService;
import like.lion.way.feed.domain.Question;
import like.lion.way.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    @Override
    public Chat findByQuestion(Question question) {
        return chatRepository.findFirstByQuestionOrderByCreatedAtDesc(question);
    }

    @Override
    public Chat createChat(Question question) {
        Chat chat = new Chat();
        chat.setUser1(question.getAnswerer());
        chat.setUser2(question.getQuestioner());
        chat.setQuestion(question);
        chat.setName(question.getQuestion());
        chat.setCreatedAt(LocalDateTime.now());

        return chatRepository.save(chat);
    }

    @Override
    public String userLeave(Chat chat, Long userId) {

        if (chat.isUser1(userId)) {
            chat.setUserActive1(false);
        } else {
            chat.setUserActive2(false);
        }

        if (!chat.userExist()) {
            messageRepository.deleteByChatId(chat.getId());
            chatRepository.delete(chat);

            return "delete";
        } else {
            chatRepository.save(chat);
            return "leave";
        }
    }

    @Override
    public List<Chat> findUserChatList(User user) {
        return chatRepository.findByUser1AndUserActive1TrueOrUser2AndUserActive2True(user, user);
    }

    @Override
    public Chat findById(Long chatId) {
        return chatRepository.findById(chatId).orElse(null);
    }
}
