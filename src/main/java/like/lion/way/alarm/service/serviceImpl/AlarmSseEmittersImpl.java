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
    private final Map<Long, Map<String, SseEmitter>> emitters = new ConcurrentHashMap<>(); // thread-safe

    @Autowired
    public AlarmSseEmittersImpl(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    public SseEmitter add(Long userId, String windowId) {
        var userEmitters = this.emitters.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());
        SseEmitter emitter = userEmitters.computeIfAbsent(windowId, k -> new SseEmitter(10 * 60 * 1000L)); // 10분

        log.debug("[SseEmitters][add] userId={}, windowId={}", userId, windowId);
        log.debug("[SseEmitters][add] number of emitters: {}", userEmitters.size());

        // 첫 데이터
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

        var userEmitters = this.emitters.get(userId);
        if (userEmitters == null) {
            log.debug("[SseEmitters][send] userEmitters is null");
            return;
        }

        // 전송할 데이터 : 해당 유저의 알람의 개수
        Long count = alarmService.countAlarm(userId);
        for (SseEmitter emitter : userEmitters.values()) {
            send(emitter, count);
        }
    }

    public synchronized void send(SseEmitter emitter, Long count) {
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
