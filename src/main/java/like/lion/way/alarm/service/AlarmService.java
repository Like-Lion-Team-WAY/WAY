package like.lion.way.alarm.service;

import like.lion.way.alarm.domain.Alarm;
import like.lion.way.alarm.domain.AlarmType;
import like.lion.way.alarm.event.AlarmEvent;
import like.lion.way.user.domain.User;

public interface AlarmService {
    /**
     * 타입에 따른 알림 세팅 설정 여부 조회
     */
    boolean isAlarmEnabled(User user, AlarmType type);

    /**
     * 알림 타입에 따른 메시지, url 생성
     */
    Alarm createAlarm(AlarmEvent alarmEvent);

    /**
     * 알림 저장
     */
    void saveAlarm(Alarm alarm);

    /**
     * 특정 유저의 알림 개수 조회
     */
    Long countAlarm(User user);
    Long countAlarm(Long userId);
}
