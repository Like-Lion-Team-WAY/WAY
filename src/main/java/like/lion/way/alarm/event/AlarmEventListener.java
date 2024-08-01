package like.lion.way.alarm.event;

import like.lion.way.alarm.service.AlarmService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@AllArgsConstructor // for final field DI
public class AlarmEventListener {
    private final AlarmService alarmService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) // 서비스의 트랜잭션 커밋 이후에 이벤트 처리
    @Transactional(propagation = Propagation.REQUIRES_NEW) // 트랜잭션을 새로 시작 -> 알림 테이블 수정이 필요하기 때문
    public void handleAlarmEvent(AlarmEvent alarmEvent) {
        log.info("[AlarmEventListener] : ");

        // 1. 알림 여부
        boolean alarmEnabled = alarmService.isAlarmEnabled(alarmEvent.getUser(), alarmEvent.getType());
        if (!alarmEnabled)
            return;

        // 2. 알림 생성, message와 url 설정
        alarmService.createAlarm(alarmEvent.getUser(), alarmEvent.getType());

        // 3. 알림 테이블에 알림 정보 저장하기

        // 3. SSE를 사용하여 클라이언트로 알람 전송하기

    }
}
