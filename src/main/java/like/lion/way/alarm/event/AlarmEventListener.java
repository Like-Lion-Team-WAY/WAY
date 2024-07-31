package like.lion.way.alarm.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AlarmEventListener {
    @Async
    @EventListener
    public void handleAlarmEvent(AlarmEvent alarmEvent) {
        // SSE를 사용하여 클라이언트로 알람 전송하기
        log.info("[AlarmEventListener] : " + alarmEvent.getMessage() + " ::: " + alarmEvent.getUrl());
    }
}
