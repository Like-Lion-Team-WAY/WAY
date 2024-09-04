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

    /**
     * SSE 구독 신청
     * @param userId 신청자 userId
     * @param windowId 신청하는 창의 고유 ID
     * @return
     */
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

    /**
     * 구독 후 첫 데이터 (알람 개수, 채팅 개수) 전송
     * @param userId 구독자 userId
     */
    @Override
    public void sendSubscriptions(Long userId) {
        Map<String, Object> data = new HashMap<>();
        data.put("count", alarmService.countAlarm(userId));
        data.put("chat", chatAlarmService.getCount(userId));

        sendJsonData(userId, "subscription", data);
    }

    /**
     * 알람 개수 전송 (헤더)
     * @param userId 구독자 userId
     */
    @Override
    public void sendAlarmCount(Long userId) {
        Map<String, Object> data = new HashMap<>();
        data.put("count", alarmService.countAlarm(userId));

        sendJsonData(userId, "count", data);
    }

    /**
     * 채팅 개수 전송 (헤더)
     * @param userId 구독자 userId
     */
    @Override
    public void sendChatCount(Long userId) {
        Map<String, Object> data = new HashMap<>();
        data.put("chat", chatAlarmService.getCount(userId));

        sendJsonData(userId, "chat", data);
    }

    /**
     * 새로운 알람 전송
     * @param userId 구독자 userId
     * @param alarm 전송할 알람
     */
    @Override
    public void sendAlarm(Long userId, Alarm alarm) {
        Map<String, Object> data = new HashMap<>();
        data.put("count", alarmService.countAlarm(userId));
        data.put("alarm", new AlarmMessageDto(alarm));

        sendJsonData(userId, "alarm", data);
    }

    /**
     * JSON 데이터를 Kafka를 통해 전송
     * @param userId 구독자 userId
     * @param eventName subscribe, count, chat, alarm
     * @param data 전송할 데이터
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
     * Kafka에서 수신한 메시지를 클라이언트에게 전송
     * @param userId 구독자 userId
     * @param name 이벤트 subscribe, count, chat, alarm
     * @param jsonData 전송할 JSON 데이터
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

    /**
     * SSE 구독 해제
     * @param userId 구독자 userId
     * @param windowId 구독 창의 고유 ID
     */
    private void removeEmitter(Long userId, String windowId) {
        var userEmitters = this.emitters.get(userId);
        if (userEmitters != null) {
            userEmitters.remove(windowId);
            if (userEmitters.isEmpty()) {
                this.emitters.remove(userId);
            }
        }
    }

    /**
     * SSE 연결 상태 확인
     * @param emitter SseEmitter
     * @return 연결 상태
     */
    private boolean isValid(SseEmitter emitter) {
        try {
            emitter.send(SseEmitter.event().name("ping").data("keep-alive"));
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
