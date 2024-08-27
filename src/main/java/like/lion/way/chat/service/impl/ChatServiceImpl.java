package like.lion.way.chat.service.impl;

import static like.lion.way.chat.constant.ChatMessageType.DELETE;
import static like.lion.way.chat.constant.ChatMessageType.LEAVE;
import static like.lion.way.chat.constant.OpenNicknameState.NICKNAME_OPEN_STATE;

import java.time.LocalDateTime;
import java.util.List;
import like.lion.way.chat.domain.Chat;
import like.lion.way.chat.domain.Message;
import like.lion.way.chat.domain.dto.ChatFuncResultDTO;
import like.lion.way.chat.repository.ChatRepository;
import like.lion.way.chat.repository.MessageRepository;
import like.lion.way.chat.service.ChatService;
import like.lion.way.chat.service.kafka.Producer;
import like.lion.way.feed.domain.Question;
import like.lion.way.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final Producer producer;

    @Override
    public Chat findByQuestion(Question question) {
        return chatRepository.findFirstByQuestionOrderByCreatedAtDesc(question);
    }

    @Override
    public Chat createChat(Question question) {
        Chat chat = new Chat();
        chat.setAnswerer(question.getAnswerer());
        chat.setQuestioner(question.getQuestioner());
        chat.setQuestion(question);
        chat.setName(question.getQuestion());
        chat.setCreatedAt(LocalDateTime.now());

        return chatRepository.save(chat);
    }

    @Override
    public String userLeave(Chat chat, Long userId) {

        if (chat.isAnswerer(userId)) {
            chat.setAnswererActive(false);
        } else {
            chat.setQuestionerActive(false);
        }

        if (!chat.userExist()) {
            messageRepository.deleteByChatId(chat.getId());
            chatRepository.delete(chat);

            return DELETE.get();
        } else {
            chatRepository.save(chat);
            return LEAVE.get();
        }
    }

    @Override
    public void changeName(Chat chat, String name) {
        chat.setName(name);
        chatRepository.save(chat);
    }

    @Override
    public void changeNicknameOpen(Chat chat, int nicknameOpenType) {
        chat.setNicknameOpen(nicknameOpenType);
        chatRepository.save(chat);
    }

    @Override
    public void withdrawProcessing(User user) {
        Long userId = user.getUserId();
        List<Chat> chats = chatRepository.findByAnswererAndAnswererActiveTrueOrQuestionerAndQuestionerActiveTrue(user, user);
        for (Chat chat : chats) {
            String result = userLeave(chat, userId);
            String nickname = getNickname(chat, userId);
            String text = "[" + nickname + "] 님이 나가겼습니다.";
            Message message = new Message();
            message.setChatId(chat.getId());
            message.setText(text);
            message.setSenderId(userId);
            message.setType(result);
            producer.sendMessage(message);
        }
    }

    @Override
    public List<Chat> findUserChatList(User user) {
        return chatRepository.findByAnswererAndAnswererActiveTrueOrQuestionerAndQuestionerActiveTrue(user, user);
    }

    @Override
    public Chat findById(Long chatId) {
        return chatRepository.findById(chatId).orElse(null);
    }

    private String getNickname(Chat chat, Long userId) {
        if (chat.isAnswerer(userId)) {
            return chat.getAnswererNickname();
        } else {
            return chat.getQuestionerNickname(chat.getNicknameOpen() != NICKNAME_OPEN_STATE.get());
        }
    }
}
