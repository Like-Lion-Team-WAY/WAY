package like.lion.way.alarm.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import like.lion.way.ApiResponse;
import like.lion.way.alarm.domain.AlarmType;
import like.lion.way.alarm.dto.AlarmMessageDto;
import like.lion.way.alarm.dto.AlarmRequestDto;
import like.lion.way.alarm.service.AlarmService;
import like.lion.way.jwt.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

    /**
     * 알림 목록 조회
     * @param page 조회할 페이지
     * @param size 페이지 당 조회할 알림 수
     * @return 알림 목록
     */
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

    /**
     * 알림 전체 삭제
     * @return 삭제 성공 여부
     */
    @DeleteMapping
    public ApiResponse<Void> deletAllAlarms(HttpServletRequest request) {
        String token = jwtUtil.getCookieValue(request, "accessToken");
        if (token == null) {
            return ApiResponse.status(HttpStatus.UNAUTHORIZED);
        }
        Long loginId = jwtUtil.getUserIdFromToken(token);

        alarmService.deleteAllAlarms(loginId);
        return ApiResponse.ok();
    }

    /**
     * 알림 삭제
     * @param id 삭제할 알림 id
     * @return 삭제 성공 여부
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlarm(@PathVariable(value = "id") Long id) {
        alarmService.deleteAlarm(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 알림 설정 조회
     * @return 알림 설정 정보
     */
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

    /**
     * 알림 설정 변경
     * @param alarmRequestDto 변경할 알림 설정 정보
     * @return 변경한 알림 설정 정보
     */
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
