package like.lion.way.alarm.service.serviceImpl;

import like.lion.way.alarm.domain.Alarm;
import like.lion.way.alarm.domain.AlarmType;
import like.lion.way.alarm.repository.AlarmRepository;
import like.lion.way.alarm.repository.AlarmSettingRepository;
import like.lion.way.alarm.service.AlarmService;
import like.lion.way.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AlarmServiceImpl implements AlarmService {
    private final AlarmSettingRepository alarmSettingRepository;
    private final AlarmRepository alarmRepository;

    @Override
    public boolean isAlarmEnabled(User user, AlarmType type) {
        return alarmSettingRepository.findByUser(user).isAlarmEnabled(type);
    }

    @Override
    public void createAlarm(User user, AlarmType type) {
        // 알림 생성
        String message = "";
        String url = "";
        Alarm alarm = new Alarm();
    }
}
