package like.lion.way.alarm.service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import like.lion.way.alarm.domain.Alarm;
import like.lion.way.alarm.dto.AlarmMessageDto;
import like.lion.way.alarm.service.AlarmService;
import like.lion.way.alarm.service.AlarmSseEmitters;
import like.lion.way.alarm.service.ChatAlarmService;
import like.lion.way.alarm.service.kafka.SseEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@Slf4j
@RequiredArgsConstructor
public class AlarmSseEmittersImpl implements AlarmSseEmitters {
    private final AlarmService alarmService;
    private final ChatAlarmService chatAlarmService;
    private final SseEventProducer kafkaSseEventProducer;  // Kafka Producer 주입
    private final Map<Long, Map<String, SseEmitter>> emitters = new ConcurrentHashMap<>(); // thread-safe

    public SseEmitter add(Long userId, String windowId) {
        var userEmitters = this.emitters.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());
        SseEmitter emitter = new SseEmitter(10 * 60 * 1000L); // 10분
        userEmitters.put(windowId, emitter);

        // 클라이언트에게 첫 데이터를 전송합니다.
        sendSubscriptions(userId);

        // 콜백 설정
        emitter.onCompletion(() -> {
            log.debug("[SseEmitters] onComplete callback");
            removeEmitter(userId, windowId);
        });
        emitter.onTimeout(() -> {
            log.debug("[SseEmitters] onTimeout callback");
            removeEmitter(userId, windowId);
        });
        emitter.onError((ex) -> {
            log.error("[SseEmitters] onError callback: {}", ex.getMessage());
            removeEmitter(userId, windowId);
        });

        return emitter;
    }

    @Override
    public void sendSubscriptions(Long userId) {
        Map<String, Object> data = new HashMap<>();
        data.put("count", alarmService.countAlarm(userId));
        data.put("chat", chatAlarmService.getCount(userId));

        sendJsonData(userId, "subscription", data);
    }

    @Override
    public void sendAlarmCount(Long userId) {
        Map<String, Object> data = new HashMap<>();
        data.put("count", alarmService.countAlarm(userId));

        sendJsonData(userId, "count", data);
    }

    @Override
    public void sendChatCount(Long userId) {
        Map<String, Object> data = new HashMap<>();
        data.put("chat", chatAlarmService.getCount(userId));

        sendJsonData(userId, "chat", data);
    }

    @Override
    public void sendAlarm(Long userId, Alarm alarm) {
        Map<String, Object> data = new HashMap<>();
        data.put("count", alarmService.countAlarm(userId));
        data.put("alarm", new AlarmMessageDto(alarm));

        sendJsonData(userId, "alarm", data);
    }

    /**
     * JSON 데이터를 Kafka를 통해 전송합니다.
     */
    private void sendJsonData(Long userId, String eventName, Map<String, Object> data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = objectMapper.writeValueAsString(data);

            // Kafka를 통해 이벤트 전송
            kafkaSseEventProducer.sendEvent(userId, eventName, jsonData);
        } catch (Exception e) {
            log.error("[SseEmitters][sendJsonData] JSON 변환 오류: {}", e.getMessage());
        }
    }

    /**
     * Kafka에서 수신한 메시지를 클라이언트에게 전송합니다.
     */
    @Override
    public synchronized void send(Long userId, String name, String jsonData) {
        var userEmitters = this.emitters.get(userId);
        if (userEmitters == null) {
            log.debug("[SseEmitters][send] userEmitters is null");
            return;
        }

        userEmitters.forEach((windowId, emitter) -> {
            if (isValid(emitter)) {
                try {
                    emitter.send(SseEmitter.event()
                            .name(name)
                            .data(jsonData));
                    log.debug("[SseEmitters][send] succeeded !!!");
                } catch (IOException | IllegalStateException e) {
                    log.error("[SseEmitters][send] Exception: {}", e.getMessage());
                    emitter.completeWithError(e);
                } catch (Exception e) {
                    log.error("[SseEmitters][send]error: {}", e.getMessage());
                    emitter.complete();
                }
            } else {
                removeEmitter(userId, windowId);
            }
        });
    }

    private void removeEmitter(Long userId, String windowId) {
        var userEmitters = this.emitters.get(userId);
        if (userEmitters != null) {
            userEmitters.remove(windowId);
            if (userEmitters.isEmpty()) {
                this.emitters.remove(userId);
            }
        }
    }

    private boolean isValid(SseEmitter emitter) {
        try {
            emitter.send(SseEmitter.event().name("ping").data("keep-alive"));
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
