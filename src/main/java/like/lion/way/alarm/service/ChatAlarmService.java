package like.lion.way.alarm.service;

import like.lion.way.user.domain.User;
import org.springframework.stereotype.Service;

@Service
public interface ChatAlarmService {
    public void countChatAlarm(User user);
    public void countChatAlarm(User user, Long count);
    public Long getCount(Long userId);
}
