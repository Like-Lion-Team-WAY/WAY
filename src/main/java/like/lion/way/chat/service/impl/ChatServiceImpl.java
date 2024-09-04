package like.lion.way.chat.service.impl;

import static like.lion.way.chat.constant.ChatMessageType.DELETE;
import static like.lion.way.chat.constant.ChatMessageType.LEAVE;
import static like.lion.way.chat.constant.OpenNicknameState.NICKNAME_OPEN_STATE;

import java.time.LocalDateTime;
import java.util.List;
import like.lion.way.chat.domain.Chat;
import like.lion.way.chat.repository.ChatRepository;
import like.lion.way.chat.repository.MessageRepository;
import like.lion.way.chat.service.ChatService;
import like.lion.way.chat.service.MessageService;
import like.lion.way.feed.domain.Question;
import like.lion.way.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 채팅 관련 서비스
 *
 * @author Lee NaYeon
 */
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final MessageService messageService;

    /**
     * 질문으로 부터 채팅방 찾기
     *
     * @param question 채팅방 찾기 위한 질문
     * @return 찾은 채팅방 데이터
     */
    @Override
    public Chat findByQuestion(Question question) {
        return chatRepository.findFirstByQuestionOrderByCreatedAtDesc(question);
    }

    /**
     * 질문으로 부터 채팅방 생성
     *
     * @param question 채팅방 생성 위한 질문
     * @return 생성된 채팅방 데이터
     */
    @Override
    public Chat createChat(Question question) {
        Chat chat = new Chat();
        chat.setAnswerer(question.getAnswerer());
        chat.setQuestioner(question.getQuestioner());
        chat.setQuestion(question);
        chat.setName(question.getQuestion());
        chat.setCreatedAt(LocalDateTime.now());
        if (!question.getIsAnonymous()) {
            chat.setNicknameOpen(NICKNAME_OPEN_STATE.get());
        }

        return chatRepository.save(chat);
    }

    /**
     * 채팅방 유저 나가기
     *
     * @param chat 나가기는 채팅방
     * @param userId 나가는 유저 Id
     * @return 나간 결과 (모든 유저 비활성 시 : delete / 단순 나가기 : leave)
     */
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

    /**
     * 채팅방 이름 변경
     *
     * @param chat 변경할 채팅방
     * @param name 변경할 이름
     */
    @Override
    public void changeName(Chat chat, String name) {
        chat.setName(name);
        chatRepository.save(chat);
    }

    /**
     * 채팅방 닉네임 요청 상태 변경
     *
     * @param chat 변경할 채팅방
     * @param nicknameOpenType 변경될 상태
     */
    @Override
    public void changeNicknameOpen(Chat chat, int nicknameOpenType) {
        chat.setNicknameOpen(nicknameOpenType);
        chatRepository.save(chat);
    }

    /**
     * 회원 탈퇴시 채팅방 데이터 처리
     *
     * @param user 탈퇴한 유저
     */
    @Override
    public void withdrawProcessing(User user) {
        Long userId = user.getUserId();
        List<Chat> chats = chatRepository.findByAnswererAndAnswererActiveTrueOrQuestionerAndQuestionerActiveTrue(user,
                user);
        for (Chat chat : chats) {
            String result = userLeave(chat, userId);
            messageService.createWithdrawalMessage(chat, userId, result);
        }
    }

    /**
     * 유저의 채팅방 리스트 찾기
     *
     * @param user 유저
     * @return 찾은 채팅방 리스트
     */
    @Override
    public List<Chat> findUserChatList(User user) {
        return chatRepository.findByAnswererAndAnswererActiveTrueOrQuestionerAndQuestionerActiveTrue(user, user);
    }

    /**
     * Id로 채팅방 찾기
     *
     * @param chatId 찾을 채팅방 Id
     * @return 찾은 채팅방
     */
    @Override
    public Chat findById(Long chatId) {
        return chatRepository.findById(chatId).orElse(null);
    }
}
