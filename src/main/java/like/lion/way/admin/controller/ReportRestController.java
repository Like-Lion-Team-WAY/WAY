package like.lion.way.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import like.lion.way.admin.domain.ReportRequestDto;
import like.lion.way.admin.service.ReportService;
import like.lion.way.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor // for constructor injection
public class ReportRestController {
    private final ReportService reportService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<Void> report(HttpServletRequest request,
                                       @RequestBody ReportRequestDto reportRequestDto) {
        // 로그인 여부 확인
        String token = jwtUtil.getCookieValue(request, "accessToken");
        if (token == null) {
            return ResponseEntity.status(401).build();
        }
        Long loginId = jwtUtil.getUserIdFromToken(token);

        try {
            reportService.report(loginId, reportRequestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(500).build();
        }

        return ResponseEntity.ok().build();
    }
}
