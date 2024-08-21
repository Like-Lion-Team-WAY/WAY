package like.lion.way.chat.service;

import like.lion.way.alarm.service.AlarmSseEmitters;
import like.lion.way.chat.domain.ChatAlarm;
import like.lion.way.chat.repository.ChatAlarmRepository;
import like.lion.way.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public interface ChatAlarmService {
    public void countChatAlarm(User user);
    public void countChatAlarm(User user, Long count);
    public void sendChatAlarm(User toUser, Long count);

}
