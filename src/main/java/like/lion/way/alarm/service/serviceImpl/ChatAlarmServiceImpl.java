package like.lion.way.alarm.service.serviceImpl;

import like.lion.way.alarm.service.AlarmSseEmitters;
import like.lion.way.alarm.domain.ChatAlarm;
import like.lion.way.alarm.repository.ChatAlarmRepository;
import like.lion.way.alarm.service.ChatAlarmService;
import like.lion.way.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatAlarmServiceImpl implements ChatAlarmService {
    private final ChatAlarmRepository chatAlarmRepository;

    /**
     * 채팅 알람 개수 1 증가
     * @param user 알람을 받을 대상
     */
    @Override
    @Transactional
    public void countChatAlarm(User user) {
        countChatAlarm(user, 1L);
    }

    /**
     * 채팅 알람 개수 증가
     * @param user 알람을 받을 대상
     * @param count 증가할 개수
     */
    @Override
    @Transactional
    public void countChatAlarm(User user, Long count) {
        var chatAlarm = chatAlarmRepository.findByUser(user).orElseGet(() -> new ChatAlarm(user));
        chatAlarm.count(count);
        chatAlarmRepository.save(chatAlarm);
    }

    /**
     * 채팅 알람 개수 조회
     * @param userId 알람을 받을 대상
     * @return 채팅 알람 개수
     */
    @Transactional(readOnly = true)
    public Long getCount(Long userId) {
        return chatAlarmRepository.findByUser_UserId(userId).map(ChatAlarm::getCount).orElse(0L);
    }
}
