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
@RequestMapping("/api")
@RequiredArgsConstructor
public class BlueCheckRestController {

    private final BlueCheckService blueCheckService;

    @PostMapping("/bluecheck/application")
    public ApiResponse<BlueCheck> blueCheckApplication(@RequestBody BlueCheckDto blueCheckDto) {
        try {
            BlueCheck blueCheck = blueCheckService.applyBlueCheck(blueCheckDto.getUserId());
            return ApiResponse.ok(blueCheck);
        } catch (Exception e) {
            return ApiResponse.statusAndData(HttpStatus.CONFLICT, null);
        }
    }

    @DeleteMapping("/bluecheck")
    public ApiResponse<Void> blueCheckCancel(@RequestParam("username") String username) {
        try {
            blueCheckService.removeBlueCheck(username);
            return ApiResponse.ok();
        } catch (Exception e) {
            return ApiResponse.status(HttpStatus.CONFLICT);
        }
    }
}
