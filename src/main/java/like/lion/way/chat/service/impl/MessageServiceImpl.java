package like.lion.way.chat.service.impl;

import static like.lion.way.chat.constant.ChatMessageType.CREATE;
import static like.lion.way.chat.constant.ChatMessageType.WITHDRAWAL;
import static like.lion.way.chat.constant.OpenNicknameState.NICKNAME_OPEN_STATE;

import java.time.LocalDateTime;
import java.util.List;
import like.lion.way.alarm.event.ChatAlarmEvent;
import like.lion.way.chat.domain.Chat;
import like.lion.way.chat.domain.Message;
import like.lion.way.chat.repository.MessageRepository;
import like.lion.way.chat.service.MessageService;
import like.lion.way.chat.service.kafka.Producer;
import like.lion.way.user.domain.User;
import like.lion.way.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 메세지 관련 서비스
 *
 * @author Lee NaYeon
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final Producer producer;
    private final ApplicationEventPublisher publisher;

    /**
     * 채팅방의 마지막 메세지 찾기
     *
     * @param id 채팅방 Id
     * @return 마지막 메세지
     */
    @Override
    public Message findLastByChatId(Long id) {
        return messageRepository.findFirstByChatIdOrderByCreatedAtDesc(id);
    }

    /**
     * 채팅방의 메세지 찾기 (pageable)
     *
     * @param chatId 채팅방 Id
     * @param pageable 페이징 정보
     * @return 메세지 데이터
     */
    @Override
    public Page<Message> findAllByChatId(Long chatId, Pageable pageable) {
        return messageRepository.findAllByChatId(chatId, pageable);
    }

    /**
     * 이전 불러온 메세지를 기준으로 채팅방 메세지 찾기 (pageable)
     *
     * @param chatId 채팅방 Id
     * @param lastLoadMessageId 이전 불러온 마지막 메세지 Id
     * @param pageable 페이징 정보
     * @return 메세지 데이터
     */
    @Override
    public Page<Message> findAllByChatIdAndIdLessThan(Long chatId, String lastLoadMessageId, Pageable pageable) {
        return messageRepository.findAllByChatIdAndIdLessThan(chatId, lastLoadMessageId, pageable);
    }

    /**
     * 채팅 시작 메세지 생성<br/>
     * 상대에게 알림 전송
     *
     * @param chat 메세지가 소속될 채팅방
     */
    @Override
    public void createStartMessage(Chat chat) {
        User chatMaker = chat.getAnswerer();

        Message message = new Message();
        message.setChatId(chat.getId());
        message.setSenderId(chatMaker.getUserId());
        message.setReceiverId(chat.getQuestionerId());
        message.setText("[" + chatMaker.getNickname() + "] 님이 채팅을 시작했습니다");
        message.setType(CREATE.get() + chat.getId());
        message.setCreatedAt(LocalDateTime.now());

        messageRepository.save(message);

        message.setChatId(0L);
        producer.sendMessage(message);
        ChatAlarmEvent event = new ChatAlarmEvent(
                this, userRepository.findById(message.getReceiverId()).orElse(null), 1L);
        publisher.publishEvent(event);
    }

    /**
     * 탈퇴시 나가기 메세지 생성<br/>
     * 메세지 보내기
     *
     * @param chat 메세지가 소속될 채팅방
     * @param userId 보낸 유저 Id
     * @param result 나가기 타입 (leave, delete)
     */
    @Override
    public void createWithdrawalMessage(Chat chat, Long userId, String result) {
        String nickname = getNickname(chat, userId);
        String text = "[" + nickname + "] 님이 나가셨습니다.";
        Message message = new Message();
        message.setChatId(chat.getId());
        message.setText(text);
        message.setSenderId(userId);
        message.setType(result + WITHDRAWAL.get());
        producer.sendMessage(message);
    }

    /**
     * 메세지 읽음 처리<br/>
     * 알림 전송
     *
     * @param userId 읽은 유저 Id
     * @param chatId 읽은 메세지가 소속된 채팅방 Id
     */
    @Override
    @Async
    public void readMessage(Long userId, Long chatId) {
        List<Message> messages;
        if ((messages = messageRepository.findByChatIdAndReceiverIdAndIsReadFalse(chatId, userId)) != null) {
            for (Message message : messages) {
                message.setIsRead(true);
            }
            messageRepository.saveAll(messages);
        }
        ChatAlarmEvent event = new ChatAlarmEvent(this, userRepository.findById(userId).orElse(null),
                messages.size() * -1L);
        publisher.publishEvent(event);
    }

    /**
     * Id 통해서 메세지 찾기
     *
     * @param id 메세지 Id
     * @return 찾은 메세지
     */
    @Override
    public Message findById(String id) {
        return messageRepository.findById(id).orElse(null);
    }

    /**
     * 닉네임 오픈 상태에 따른 유저 닉네임 추출
     *
     * @param chat 닉네임 오픈 상태 확인 위한 채팅방 정보
     * @param userId 닉네임 추출할 유저 Id
     * @return 닉네임
     */
    private String getNickname(Chat chat, Long userId) {
        if (chat.isAnswerer(userId)) {
            return chat.getAnswererNickname();
        } else {
            return chat.getQuestionerNickname(chat.getNicknameOpen() != NICKNAME_OPEN_STATE.get());
        }
    }
}
