package like.lion.way.alarm.event;

import like.lion.way.alarm.domain.Alarm;
import like.lion.way.alarm.service.AlarmService;
import like.lion.way.alarm.service.AlarmSseEmitters;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@AllArgsConstructor // for final field DI
public class AlarmEventListener {
    private final AlarmService alarmService;
    private final AlarmSseEmitters emitters;

    @Async("asyncTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) // 서비스의 트랜잭션 커밋 이후에 이벤트 처리
    // @EventListener
    public void handleAlarmEvent(AlarmEvent alarmEvent) {
        // 1. 알림 여부
        boolean alarmEnabled = alarmService.isAlarmEnabled(alarmEvent.getToUser(), alarmEvent.getType());
        if (!alarmEnabled) {
            return;
        }

        // 2. 알림 생성, message와 url 설정
        Alarm alarm = alarmService.createAlarm(alarmEvent);
        if (alarm == null) {
            return;
        }

        // 3. 알림 테이블에 알림 정보 저장하기
        alarmService.saveAlarm(alarm);

        // 3. SSE를 사용하여 클라이언트로 알람 전송하기
        Long userId = alarm.getUser().getUserId();
        emitters.sendAlarm(userId, alarm);
    }
}
