package like.lion.way.alarm.event;

import like.lion.way.alarm.service.AlarmSseEmitters;
import like.lion.way.alarm.service.ChatAlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor // 생성자 주입
public class ChatAlarmEventListener {
    private final ChatAlarmService chatAlarmService;
    private final AlarmSseEmitters alarmSseEmitters;

    @Async("asyncTaskExecutor")
    @EventListener
    public void handleChatAlarmEvent(ChatAlarmEvent event) {
        Long userId = event.getUser().getUserId();
        chatAlarmService.countChatAlarm(event.getUser(), event.getGap());
        alarmSseEmitters.sendChatCount(userId);
    }
}
