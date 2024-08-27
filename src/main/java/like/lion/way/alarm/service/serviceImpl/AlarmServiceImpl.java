package like.lion.way.alarm.service.serviceImpl;

import java.util.HashMap;
import like.lion.way.alarm.domain.Alarm;
import like.lion.way.alarm.domain.AlarmSetting;
import like.lion.way.alarm.domain.AlarmType;
import like.lion.way.alarm.dto.AlarmMessageDto;
import like.lion.way.alarm.event.AlarmEvent;
import like.lion.way.alarm.repository.AlarmRepository;
import like.lion.way.alarm.repository.AlarmSettingRepository;
import like.lion.way.alarm.service.AlarmService;
import like.lion.way.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

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
        String message =
                alarmEvent.getFromUser() == null ? type.getMessage("익명")
                        : type.getMessage(alarmEvent.getFromUser().getNickname());
        String url = type.getUrl(alarmEvent.getPathVariable());
        log.info("[AlarmService] create alarm to : {}", alarmEvent.getToUser().getNickname());
        return new Alarm(alarmEvent.getToUser(), message, url);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAlarm(Alarm alarm) {
        log.debug("[AlarmService] save alarm");
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

    @Override
    @Transactional(readOnly = true)
    public Page<AlarmMessageDto> getAlarm(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return alarmRepository.findByUser_UserId(userId, pageable)
                .map(AlarmMessageDto::new);
    }

    @Override
    @Transactional
    public void deleteAlarm(Long alarmId) {
        alarmRepository.deleteById(alarmId);
    }

    @Override
    @Transactional
    public void deleteAllAlarms(Long userId) {
        alarmRepository.deleteByUser_UserId(userId);
    }

    @Override
    @Transactional
    public void updateAlarmSetting(Long userId, AlarmType type, boolean enabled) {
        AlarmSetting alarmSetting = alarmSettingRepository.findByUser_UserId(userId).orElseThrow();

        switch (type) {
            case NEW_QUESTION -> alarmSetting.setNewQuestion(enabled);
            case REPLY -> alarmSetting.setReply(enabled);
            case COMMENT -> alarmSetting.setComment(enabled);
            case ANSWER -> alarmSetting.setAnswer(enabled);
            case BOARD_COMMENT -> alarmSetting.setBoardComment(enabled);
        }
        alarmSettingRepository.save(alarmSetting);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Boolean> getAlarmSetting(Long userId) {
        Map<String, Boolean> settings = new HashMap<>();
        AlarmSetting alarmSetting = alarmSettingRepository.findByUser_UserId(userId).orElseThrow();

        settings.put(AlarmType.NEW_QUESTION.getType(), alarmSetting.isNewQuestion());
        settings.put(AlarmType.ANSWER.getType(), alarmSetting.isAnswer());
        settings.put(AlarmType.COMMENT.getType(), alarmSetting.isComment());
        settings.put(AlarmType.REPLY.getType(), alarmSetting.isReply());

        return settings;
    }
}
