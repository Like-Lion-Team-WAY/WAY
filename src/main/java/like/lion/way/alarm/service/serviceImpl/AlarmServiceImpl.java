package like.lion.way.alarm.service.serviceImpl;

import java.util.Optional;
import like.lion.way.alarm.domain.Alarm;
import like.lion.way.alarm.domain.AlarmSetting;
import like.lion.way.alarm.domain.AlarmType;
import like.lion.way.alarm.event.AlarmEvent;
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
        return alarmSettingRepository.findByUser(user)
                .map(alarmSetting -> getAlarmStatus(alarmSetting, type))
                .orElse(false);
    }

    private boolean getAlarmStatus(AlarmSetting alarmSetting, AlarmType alarmType) {
        switch (alarmType) {
            case NEW_QUESTION:
                return alarmSetting.isNewQuestion();
            case REPLY:
                return alarmSetting.isReply();
            case COMMENT:
                return alarmSetting.isComment();
            case ANSWER:
                return alarmSetting.isAnswer();
            case BOARD_COMMENT:
                return alarmSetting.isBoardComment();
            default:
                return false;
        }
    }

    @Override
    public void createAlarm(AlarmEvent alarmEvent) {
        // 1. 메시지 생성


    }

    private void getAlarmMessage(AlarmType type) {
        switch (type) {
            case
        }
    }
}
