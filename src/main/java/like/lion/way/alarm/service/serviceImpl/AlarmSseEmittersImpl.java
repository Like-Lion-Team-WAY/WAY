package like.lion.way.alarm.service.serviceImpl;

import java.io.IOException;
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
        SseEmitter emitter;

        if (this.emitters.containsKey(userId)) {
            emitter = this.emitters.get(userId);
            log.debug("[SseEmitters][add] already exists emitter!");
        } else {
            emitter = new SseEmitter(30 * 60 * 1000L); // 30분
            this.emitters.put(userId, emitter);
            log.debug("[SseEmitters][add] create new emitter!");
            log.debug("[SseEmitters][add] list size: {}", emitters.size());
        }

        // 첫 데이터
        Long count = alarmService.countAlarm(userId);
        send(userId);

        // set callbacks
        emitter.onCompletion(() -> {
            log.debug("[SseEmitters] onComplete callback");
            this.emitters.remove(userId);
        });
        emitter.onTimeout(() -> {
            log.debug("[SseEmitters] onTimeout callback");
            this.emitters.remove(userId);
        });
        emitter.onError((ex) -> {
            log.error("[SseEmitters] onError callback: {}", ex.getMessage());
            emitter.complete();
        });

        return emitter;
    }

    public void send(Long userId) {
        log.debug("[SseEmitters][send] try to send to no.{} user", userId);

        SseEmitter emitter = this.emitters.get(userId);
        if (emitter == null) {
            log.debug("[SseEmitters][send] emitter is null");
            return;
        }

        // 전송할 데이터 : 해당 유저의 알람의 개수
        Long count = alarmService.countAlarm(userId);
        send(emitter, count);
    }

    public synchronized void send(SseEmitter emitter, Long count) {
        log.debug("[SseEmitters][send] emitter exists");
        // 전송
        try {
            emitter.send(SseEmitter.event()
                    .name("count")
                    .data(count));
            log.debug("[SseEmitters][send] succeeded !!!  : {}", count);
        } catch (IllegalStateException e) {
            log.error("[SseEmitters][send] IllegalStateException: {}", e.getMessage());
            emitter.completeWithError(e);
        } catch (IOException e) {
            log.error("[SseEmitters][send] IOException: {}", e.getMessage());
            emitter.completeWithError(e);
        } catch (Exception e) {
            log.error("[SseEmitters][send]error: {}", e.getMessage());
            emitter.complete();
        }
    }
}
