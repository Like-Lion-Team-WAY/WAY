package like.lion.way.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import like.lion.way.ApiResponse;
import like.lion.way.admin.dto.ReportRequestDto;
import like.lion.way.admin.dto.ReportResponseDto;
import like.lion.way.admin.service.ReportService;
import like.lion.way.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor // for constructor injection
@RequestMapping("/api/report")
public class ReportRestController {
    private final ReportService reportService;
    private final JwtUtil jwtUtil;

    /**
     * 신고 신청 api
     * @param reportRequestDto 신고 신청 정보
     * @return 신고 신청 결과
     */
    @PostMapping()
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

    /**
     * 신고 신청 목록 조회 api
     * @param type 신고 타입
     * @param reported 신고 당한 사용자
     * @param sortDirection 정렬 방향
     * @return 필터링 된 신고 신청 목록
     */
    @GetMapping()
    public ApiResponse<List<ReportResponseDto>> getReports(@RequestParam(required = false) String type,
                                                           @RequestParam(required = false) String reported,
                                                           @RequestParam(required = false) String sortDirection) {
        List<ReportResponseDto> reports = reportService.getReports(type, reported, sortDirection);
        return ApiResponse.ok(reports);
    }

    /**
     * 신고 신청 삭제 api
     * @param id 삭제할 신고 신청 id
     * @return 삭제 결과
     */
    @DeleteMapping()
    public ApiResponse<Void> deleteReport(@RequestParam Long id) {
        reportService.deleteReport(id);
        return ApiResponse.ok(null);
    }
}
