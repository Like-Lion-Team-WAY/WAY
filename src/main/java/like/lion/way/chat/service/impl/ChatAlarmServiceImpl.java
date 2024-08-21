package like.lion.way.chat.service.impl;

import like.lion.way.alarm.service.AlarmSseEmitters;
import like.lion.way.chat.domain.ChatAlarm;
import like.lion.way.chat.repository.ChatAlarmRepository;
import like.lion.way.chat.service.ChatAlarmService;
import like.lion.way.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatAlarmServiceImpl implements ChatAlarmService {
    private final ChatAlarmRepository chatAlarmRepository;
    private final AlarmSseEmitters alarmSseEmitters;

    @Transactional
    public void countChatAlarm(User user) {
        countChatAlarm(user, 1L);
    }

    @Transactional
    public void countChatAlarm(User user, Long count) {
        var chatAlarm = chatAlarmRepository.findByUser(user).orElseGet(() -> new ChatAlarm(user));
        chatAlarm.count(count);
        chatAlarmRepository.save(chatAlarm);
        sendChatAlarm(user, chatAlarm.getCount());
    }
    public void sendChatAlarm(User toUser, Long count) {
        alarmSseEmitters.sendChatCount(toUser.getUserId(), count);
    }
}
