package like.lion.way.alarm.event;

import like.lion.way.alarm.domain.Alarm;
import like.lion.way.alarm.repository.AlarmRepository;
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
    private final AlarmRepository alarmRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) // 질문, 게시글, 채팅 트랜잭션 커밋 이후에 이벤트 처리
    @Transactional(propagation = Propagation.REQUIRES_NEW) // 트랜잭션을 새로 시작 -> 알림 테이블 수정이 필요하기 때문
    public void handleAlarmEvent(AlarmEvent alarmEvent) {
        log.info("[AlarmEventListener] : " + alarmEvent.getMessage() + " ::: " + alarmEvent.getUrl());

        // 1. 알림 테이블에 알림 정보 저장하기
        Alarm alarm = new Alarm(alarmEvent.getUser(), alarmEvent.getMessage(), alarmEvent.getUrl());
        alarmRepository.save(alarm);

        // 2. SSE를 사용하여 클라이언트로 알람 전송하기
    }
}
