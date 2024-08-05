package like.lion.way.alarm.controller;

import like.lion.way.alarm.repository.AlarmSseEmitters;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
@Slf4j
@AllArgsConstructor
public class SseController {
    private final AlarmSseEmitters emitters;

    /**
     * 클라이언트가 SSE를 구독할 때 사용하는 엔드포인트
     */
    @GetMapping(value = "/subscribe/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(@PathVariable Long userId) {
        SseEmitter emitter = emitters.add(userId);
        try {
            emitter.send(SseEmitter.event()
                    .name("subscribe")
                    .data("subscribe!"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(emitter);
    }
}
