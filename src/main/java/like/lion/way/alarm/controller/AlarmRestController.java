package like.lion.way.alarm.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import like.lion.way.alarm.domain.AlarmType;
import like.lion.way.alarm.dto.AlarmMessageDto;
import like.lion.way.alarm.dto.AlarmRequestDto;
import like.lion.way.alarm.service.AlarmService;
import like.lion.way.jwt.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alarm")
@AllArgsConstructor
@Slf4j
public class AlarmRestController {
    private final AlarmService alarmService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<Page<AlarmMessageDto>> getAlarm(HttpServletRequest request,
                                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                                          @RequestParam(value = "size", defaultValue = "10") int size) {
        // 1. jwt util로 user 정보 받아오기
        String token = jwtUtil.getCookieValue(request, "accessToken");
        if (token == null) {
            return ResponseEntity.status(401).build();
        }
        Long loginId = jwtUtil.getUserIdFromToken(token);

        // 2. user 정보로 alarm 조회 해오기
        Page<AlarmMessageDto> alarmDto = alarmService.getAlarm(loginId, page, size);

        // 4. return ResponseEntity.ok(alarmDto);
        if (alarmDto == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(alarmDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlarm(@PathVariable(value = "id") Long id) {
        alarmService.deleteAlarm(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/setting")
    public ResponseEntity<Map<String, Boolean>> getAlarmSetting(HttpServletRequest request) {
        // user 정보 받아오기
        String token = jwtUtil.getCookieValue(request, "accessToken");
        if (token == null) {
            return ResponseEntity.status(401).build();
        }
        Long loginId = jwtUtil.getUserIdFromToken(token);

        // 알림 설정 조회
        Map<String, Boolean> alarmSetting = alarmService.getAlarmSetting(loginId);
        return ResponseEntity.ok(alarmSetting);
    }

    @PutMapping("/setting")
    public ResponseEntity<Map<String, Boolean>> updateAlarmSetting(HttpServletRequest request,
                                                   @RequestBody AlarmRequestDto alarmRequestDto) {
        // user 정보 받아오기
        String token = jwtUtil.getCookieValue(request, "accessToken");
        if (token == null) {
            return ResponseEntity.status(401).build();
        }
        Long loginId = jwtUtil.getUserIdFromToken(token);

        // 알림 설정 변경
        AlarmType type = alarmRequestDto.getAlarmType();
        boolean enabled = alarmRequestDto.isEnabled();
        alarmService.updateAlarmSetting(loginId, type, enabled);

        // 응답
        Map<String, Boolean> alarmSetting = alarmService.getAlarmSetting(loginId);
        return ResponseEntity.ok(alarmSetting);
    }
}
