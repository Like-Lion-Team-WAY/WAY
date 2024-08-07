package like.lion.way.alarm.service.serviceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import like.lion.way.alarm.domain.Alarm;
import like.lion.way.alarm.service.AlarmService;
import like.lion.way.alarm.service.AlarmSseEmitters;
import like.lion.way.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@Slf4j
public class AlarmSseEmittersImpl implements AlarmSseEmitters {
    private final AlarmService alarmService;
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>(); // thread-safe

    @Autowired
    public AlarmSseEmittersImpl(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    public SseEmitter add(Long userId) {
        if (this.emitters.containsKey(userId)) {
            log.info("[SseEmitters] already exists emitter!");
            return this.emitters.get(userId);
        }

        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L); // 30분
        this.emitters.put(userId, emitter);

        log.info("[SseEmitters] create new emitter!");
        log.info("[SseEmitters] added: {}", emitter);
        log.info("[SseEmitters] list size: {}", emitters.size());

        // 첫 데이터
        Long count = alarmService.countAlarm(userId);
        send(emitter, count);

        // set callbacks
        emitter.onCompletion(() -> {
            log.info("[SseEmitters] onComplete callback");
            this.emitters.remove(userId);
        });
        emitter.onTimeout(() -> {
            log.info("[SseEmitters] onTimeout callback");
            this.emitters.remove(userId);
        });
        emitter.onError((ex) -> {
            log.error("[SseEmitters] onError callback: {}", ex.getMessage());
            this.emitters.remove(userId);
        });

        return emitter;
    }

    public void send(Long userId) {
        log.info("[SseEmitters] user Id: {}", userId);
        SseEmitter emitter = this.emitters.get(userId);
        if (emitter == null) {
            log.info("[SseEmitters] emitter is null");
            return;
        }

        // 전송할 데이터 : 해당 유저의 알람의 개수
        Long count = alarmService.countAlarm(userId);
        send(emitter, count);
    }

    public void send(SseEmitter emitter, Long count) {
        // 전송
        try {
            emitter.send(SseEmitter.event()
                    .name("count")
                    .data(count));
            log.info("[SseEmitters] send!! : {}", count);
        } catch (Exception e) {
            log.error("[SseEmitters] send error: {}", e.getMessage());
            emitter.complete();
        }
    }
}
