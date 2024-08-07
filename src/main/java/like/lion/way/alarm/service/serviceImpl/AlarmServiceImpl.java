package like.lion.way.alarm.service.serviceImpl;

import like.lion.way.alarm.domain.Alarm;
import like.lion.way.alarm.domain.AlarmSetting;
import like.lion.way.alarm.domain.AlarmType;
import like.lion.way.alarm.event.AlarmEvent;
import like.lion.way.alarm.repository.AlarmRepository;
import like.lion.way.alarm.repository.AlarmSettingRepository;
import like.lion.way.alarm.service.AlarmService;
import like.lion.way.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class AlarmServiceImpl implements AlarmService {
    private final AlarmSettingRepository alarmSettingRepository;
    private final AlarmRepository alarmRepository;

    @Override
    public boolean isAlarmEnabled(User user, AlarmType type) {
        return alarmSettingRepository.findByUser(user)
                .map(alarmSetting -> getAlarmStatus(alarmSetting, type))
                .orElse(false);
    }

    @Override
    public Alarm createAlarm(AlarmEvent alarmEvent) {
        AlarmType type = alarmEvent.getType();
        String message = type.getMessage(alarmEvent.getFromUser().getNickname());
        String url = type.getUrl(alarmEvent.getPathVariable());
        log.info("[AlarmService] user: {}", alarmEvent.getToUser().getNickname());
        return new Alarm(alarmEvent.getToUser(), message, url);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAlarm(Alarm alarm) {
        alarmRepository.save(alarm);
    }

    private boolean getAlarmStatus(AlarmSetting alarmSetting, AlarmType alarmType) {
        return switch (alarmType) {
            case NEW_QUESTION -> alarmSetting.isNewQuestion();
            case REPLY -> alarmSetting.isReply();
            case COMMENT -> alarmSetting.isComment();
            case ANSWER -> alarmSetting.isAnswer();
            case BOARD_COMMENT -> alarmSetting.isBoardComment();
            default -> false;
        };
    }

    @Override
    @Transactional(readOnly = true)
    public Long countAlarm(User user) {
        return alarmRepository.countByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countAlarm(Long userId) {
        return alarmRepository.countByUser_UserId(userId);
    }
}
