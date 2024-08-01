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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public Alarm createAlarm(AlarmEvent alarmEvent) {
        String message = getAlarmMessage(alarmEvent.getType(), alarmEvent.getFromUser().getNickname());
        String url = getAlarmUrl(alarmEvent.getType(), alarmEvent.getPathVariable());
        return new Alarm(alarmEvent.getToUser(), message, url);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAlarm(Alarm alarm) {
        alarmRepository.save(alarm);
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

    private String getAlarmMessage(AlarmType type, String fromUser) {
        switch (type) {
            case NEW_QUESTION:
                return "새 질문이 도착했습니다.";
            case REPLY:
                return fromUser + " 님이 회원님이 남긴 댓글에 대댓글을 달았습니다.";
            case COMMENT:
                return fromUser + " 님이 회원님의 글에 댓글을 달았습니다.";
            case ANSWER:
                return fromUser + " 님이 회원님이 남긴 질문에 답변을 달았습니다.";
            case BOARD_COMMENT:
                return fromUser + " 님이 회원님의 커뮤니티 게시글에 댓글을 달았습니다.";
            default:
                return "";
        }
    }

    private String getAlarmUrl(AlarmType type, String pathVariable) {
        switch (type) {
            case NEW_QUESTION:
                return "/questions/NewList/" + pathVariable;
            case REPLY, COMMENT:
                return "/posts/detail/" + pathVariable;
            case ANSWER:
                return "";
            case BOARD_COMMENT:
                return "";
            default:
                return "";
        }
    }

}
