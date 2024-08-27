package like.lion.way.admin.controller;

import like.lion.way.ApiResponse;
import like.lion.way.admin.domain.BlueCheck;
import like.lion.way.admin.domain.dto.BlueCheckDto;
import like.lion.way.admin.service.BlueCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BlueCheckRestController {

    private final BlueCheckService blueCheckService;

    @PostMapping("/bluecheck/application")
    public ApiResponse<BlueCheck> blueCheckApplication(@RequestBody BlueCheckDto blueCheckDto) {
        BlueCheck blueCheck = blueCheckService.applyBlueCheck(blueCheckDto.getUserId());
        return ApiResponse.ok(blueCheck);
    }
}
