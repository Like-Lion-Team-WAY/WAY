package like.lion.way.chat.service.impl;


import java.time.LocalDateTime;
import like.lion.way.chat.domain.Chat;
import like.lion.way.chat.repository.ChatRepository;
import like.lion.way.chat.service.ChatService;
import like.lion.way.feed.domain.Question;
import like.lion.way.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;

    @Override
    public Page<Chat> findAllByUser1OrUser2(User user, Pageable pageable) {
        return chatRepository.findAllByUser1OrUser2(user, user, pageable);
    }

    @Override
    public Chat findByQuestion(Question question) {
        return chatRepository.findByQuestion(question);
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
    public Chat findById(Long chatId) {
        return chatRepository.findById(chatId).orElse(null);
    }
}
