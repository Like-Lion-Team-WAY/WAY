package like.lion.way.alarm.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import like.lion.way.alarm.domain.Alarm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@Slf4j
public class AlarmSseEmitters {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>(); // thread-safe

    public SseEmitter add(Long userId) {
        SseEmitter emitter = new SseEmitter();
        this.emitters.put(userId, emitter);

        log.info("[SseEmitters] added: {}", emitter);
        log.info("[SseEmitters] list size: {}", emitters.size());
        log.info("[SseEmitters] list: {}", emitters);

        // set callbacks
        emitter.onCompletion(() -> {
            log.info("[SseEmitters] onComplete callback");
            this.emitters.remove(emitter);
        });
        emitter.onTimeout(() -> {
            log.info("[SseEmitters] onTimeout callback");
            emitter.complete();
        });

        return emitter;
    }

    public void send(Alarm alarm) {
        SseEmitter emitter = this.emitters.get(alarm.getUser().getUserId());
        if (emitter == null) {
            log.info("[SseEmitters] emitter is null");
            return;
        }

        try {
            emitter.send(SseEmitter.event()
                    .name("alarm")
                    .data(alarm));
        } catch (Exception e) {
            log.error("[SseEmitters] send error: {}", e.getMessage());
            emitter.complete();
        }
    }
}
