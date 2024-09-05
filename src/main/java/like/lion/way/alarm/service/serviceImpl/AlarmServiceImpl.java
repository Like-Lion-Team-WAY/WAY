package like.lion.way.alarm.service.serviceImpl;

import java.util.HashMap;
import java.util.List;
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

    /**
     * 타입에 따른 알림 세팅 설정 여부 조회
     *
     * @param user 조회할 대상자
     * @param type 알림 타입
     * @return 알림 설정 여부
     */
    @Override
    public boolean isAlarmEnabled(User user, AlarmType type) {
        return alarmSettingRepository.findByUser(user)
                .map(alarmSetting -> getAlarmStatus(alarmSetting, type))
                .orElse(false);
    }

    /**
     * 알림 타입에 따라 메시지, url 생성
     *
     * @param alarmEvent 알림 이벤트
     * @return 생성된 알림
     */
    @Override
    public Alarm createAlarm(AlarmEvent alarmEvent) {
        // 자기 자신에게 알림을 보낼 수 없음
        if (alarmEvent.getFromUser().equals(alarmEvent.getToUser())) {
            return null;
        }
        AlarmType type = alarmEvent.getType();
        String message =
                alarmEvent.getFromUser() == null ? type.getMessage("익명")
                        : type.getMessage(alarmEvent.getFromUser().getNickname());
        String url = type.getUrl(alarmEvent.getPathVariable());
        log.info("[AlarmService] create alarm to : {}", alarmEvent.getToUser().getNickname());
        return new Alarm(alarmEvent.getToUser(), message, url);
    }

    /**
     * 알림 저장
     *
     * @param alarm 저장할 알림
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAlarm(Alarm alarm) {
        log.debug("[AlarmService] save alarm");
        alarmRepository.save(alarm);
    }

    /**
     * 특정 타입의 알림 설정 여부 조회
     *
     * @param alarmSetting 알림 설정
     * @param alarmType    알림 타입
     * @return 알림 설정 여부
     */
    private boolean getAlarmStatus(AlarmSetting alarmSetting, AlarmType alarmType) {
        return switch (alarmType) {
            case NEW_QUESTION -> alarmSetting.isNewQuestion();
            case REPLY -> alarmSetting.isReply();
            case COMMENT -> alarmSetting.isComment();
            case ANSWER -> alarmSetting.isAnswer();
            case BOARD_COMMENT -> alarmSetting.isBoardComment();
            case BOARD_REPLY -> alarmSetting.isBoardReply();
            default -> false;
        };
    }

    /**
     * 특정 유저의 알림 개수 조회
     *
     * @param user 조회할 대상자
     * @return 알림 개수
     */
    @Override
    @Transactional(readOnly = true)
    public Long countAlarm(User user) {
        return alarmRepository.countByUser(user);
    }

    /**
     * 특정 유저의 알림 개수 조회
     *
     * @param userId 조회할 대상자의 userId
     * @return 알림 개수
     */
    @Override
    @Transactional(readOnly = true)
    public Long countAlarm(Long userId) {
        return alarmRepository.countByUser_UserId(userId);
    }

    /**
     * 알림 조회
     *
     * @param userId 조회할 대상자의 userId
     * @param page   페이지 번호
     * @param size   페이지 크기
     * @return 알림 목록
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AlarmMessageDto> getAlarm(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return alarmRepository.findByUser_UserId(userId, pageable)
                .map(AlarmMessageDto::new);
    }

    /**
     * 특정 알림 삭제
     *
     * @param alarmId 삭제할 알림의 id
     */
    @Override
    @Transactional
    public void deleteAlarm(Long alarmId) {
        alarmRepository.deleteById(alarmId);
    }

    /**
     * 모든 알림 삭제
     *
     * @param userId 삭제할 대상자의 userId
     */
    @Override
    @Transactional
    public void deleteAllAlarms(Long userId) {
        List<Alarm> alarms = alarmRepository.findByUser_UserId(userId);
        if (!alarms.isEmpty()) {
            alarmRepository.deleteAll(alarms);
        }
    }

    /**
     * 알림 설정 변경
     *
     * @param userId  변경할 대상자의 userId
     * @param type    변경할 알림 타입
     * @param enabled 변경할 알림 설정 여부
     */
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
            case BOARD_REPLY -> alarmSetting.setBoardReply(enabled);
        }
        alarmSettingRepository.save(alarmSetting);
    }

    /**
     * 알림 설정 조회
     *
     * @param userId 조회할 대상자의 userId
     * @return 알림 설정
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Boolean> getAlarmSetting(Long userId) {
        Map<String, Boolean> settings = new HashMap<>();
        AlarmSetting alarmSetting = alarmSettingRepository.findByUser_UserId(userId).orElseThrow();

        settings.put(AlarmType.NEW_QUESTION.getType(), alarmSetting.isNewQuestion());
        settings.put(AlarmType.ANSWER.getType(), alarmSetting.isAnswer());
        settings.put(AlarmType.COMMENT.getType(), alarmSetting.isComment());
        settings.put(AlarmType.REPLY.getType(), alarmSetting.isReply());
        settings.put(AlarmType.BOARD_COMMENT.getType(), alarmSetting.isBoardComment());
        settings.put(AlarmType.BOARD_REPLY.getType(), alarmSetting.isBoardReply());

        return settings;
    }
}
