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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@Slf4j
@RequiredArgsConstructor
public class AlarmSseEmittersImpl implements AlarmSseEmitters {
    private final AlarmService alarmService;
    private final ChatAlarmService chatAlarmService;
    private final Map<Long, Map<String, SseEmitter>> emitters = new ConcurrentHashMap<>(); // thread-safe

    public SseEmitter add(Long userId, String windowId) {
        var userEmitters = this.emitters.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());
        SseEmitter emitter = new SseEmitter(10 * 60 * 1000L); // 10분
        userEmitters.put(windowId, emitter);

//        log.debug("[SseEmitters][add] userId={}, windowId={}", userId, windowId);
//        log.debug("[SseEmitters][add] number of emitters: {}", userEmitters.size());

        // 첫 데이터 전송
        sendSubscriptions(userId);

        // set callbacks
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

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = objectMapper.writeValueAsString(data);  // JSON 문자열로 변환

            send(userId, "subscription", jsonData);
        } catch (Exception e) {
            log.error("[SseEmitters][send] JSON 변환 오류: {}", e.getMessage());
        }
    }

    @Override
    public void sendAlarmCount(Long userId) {
        Map<String, Object> data = new HashMap<>();
        data.put("count", alarmService.countAlarm(userId));

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = objectMapper.writeValueAsString(data);  // JSON 문자열로 변환

            send(userId, "count", jsonData);
        } catch (Exception e) {
            log.error("[SseEmitters][send] JSON 변환 오류: {}", e.getMessage());
        }
    }

    @Override
    public void sendChatCount(Long userId) {
        Map<String, Object> data = new HashMap<>();
        data.put("chat", chatAlarmService.getCount(userId));

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = objectMapper.writeValueAsString(data);  // JSON 문자열로 변환

            send(userId, "chat", jsonData);
        } catch (Exception e) {
            log.error("[SseEmitters][send] JSON 변환 오류: {}", e.getMessage());
        }
    }

    @Override
    public void sendAlarm(Long userId, Alarm alarm) {
        Map<String, Object> data = new HashMap<>();
        data.put("count", alarmService.countAlarm(userId));
        data.put("alarm", new AlarmMessageDto(alarm));

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = objectMapper.writeValueAsString(data);  // JSON 문자열로 변환

            send(userId, "alarm", jsonData);
        } catch (Exception e) {
            log.error("[SseEmitters][send] JSON 변환 오류: {}", e.getMessage());
        }
    }

    public void send(Long userId, String name, String jsonData) {
        // user가 emitter를 가지고 있는지 확인
        var userEmitters = this.emitters.get(userId);
        if (userEmitters == null) {
            log.debug("[SseEmitters][send] userEmitters is null");
            return;
        }

        // 전송
        for (String windowId : userEmitters.keySet()) {
            SseEmitter emitter = userEmitters.get(windowId);
            if (emitter != null) {
                if (isValid(emitter)) {
                    send(emitter, name, jsonData);
                } else {
                    removeEmitter(userId, windowId);
                }
            }
        }
    }

    public synchronized void send(SseEmitter emitter, String name, String jsonData) {
        // 전송
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
    }

    private void removeEmitter(Long userId, String windowId) {
        var userEmitters = this.emitters.get(userId);
        if (userEmitters == null) {
            return;
        }
        userEmitters.remove(windowId);
        if (userEmitters.isEmpty()) {
            this.emitters.remove(userId);
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
