package like.lion.way.admin.controller;

import like.lion.way.ApiResponse;
import like.lion.way.admin.domain.BlueCheck;
import like.lion.way.admin.domain.dto.BlueCheckDto;
import like.lion.way.admin.service.BlueCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/bluecheck")
@RequiredArgsConstructor
public class BlueCheckRestController {

    private final BlueCheckService blueCheckService;

    /**
     * 인증 배지 신청 api
     * @param blueCheckDto 인증 배지 신청 정보
     * @return 인증 배지 신청 결과
     */
    @PostMapping("/application")
    public ApiResponse<BlueCheck> blueCheckApplication(@RequestBody BlueCheckDto blueCheckDto) {
        try {
            BlueCheck blueCheck = blueCheckService.applyBlueCheck(blueCheckDto.getUserId());
            return ApiResponse.ok(blueCheck);
        } catch (Exception e) {
            return ApiResponse.statusAndData(HttpStatus.CONFLICT, null);
        }
    }

    /**
     * 인증 배지 신청 내역 삭제 api
     * @param username 삭제할 사용자 아이디
     * @return 삭제 결과
     */
    @DeleteMapping()
    public ApiResponse<Void> blueCheckCancel(@RequestParam("username") String username) {
        try {
            blueCheckService.removeBlueCheck(username);
            return ApiResponse.ok();
        } catch (Exception e) {
            return ApiResponse.status(HttpStatus.CONFLICT);
        }
    }
}
