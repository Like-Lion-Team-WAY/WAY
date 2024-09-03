package like.lion.way.alarm.controller;

import jakarta.servlet.http.HttpServletRequest;
import like.lion.way.alarm.domain.AlarmType;
import like.lion.way.alarm.domain.ChatAlarm;
import like.lion.way.alarm.event.AlarmEvent;
import like.lion.way.alarm.event.ChatAlarmEvent;
import like.lion.way.alarm.service.AlarmSseEmitters;
import like.lion.way.alarm.service.ChatAlarmService;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@Slf4j
@AllArgsConstructor
public class SseRestController {
    private final AlarmSseEmitters emitters;
    private final JwtUtil jwtUtil;
    private final ChatAlarmService chatAlarmService;

    /**
     * 클라이언트가 SSE를 구독할 때 사용하는 엔드포인트
     */
    @GetMapping(value = "/sse/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(HttpServletRequest request,
                                @RequestParam("windowId") String windowId) {
        String token = jwtUtil.getCookieValue(request, "accessToken");
        if (token == null) {
            log.debug("[SseRestController] token is null");
            return null;
        }
        Long loginId = jwtUtil.getUserIdFromToken(token);
        SseEmitter emitter = emitters.add(loginId, windowId);
        return emitter;
    }
}
