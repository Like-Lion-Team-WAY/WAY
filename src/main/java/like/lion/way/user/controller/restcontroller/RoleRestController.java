package like.lion.way.user.controller.restcontroller;

import like.lion.way.ApiResponse;
import like.lion.way.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/role")
public class RoleRestController {
    @Autowired private UserService userService;

    /**
     * 계정 정지 (LIMITED로 롤 변경)
     * @param username 정지할 사용자
     * @return 롤 변경 성공 여부
     */
    @PostMapping("/limited")
    public ApiResponse<Void> limitedRole(@RequestParam("username") String username) {
        var result = userService.addRoleLimited(username);
        if (result) {
            return ApiResponse.ok(null);
        } else {
            return ApiResponse.status(HttpStatus.CONFLICT);
        }
    }

    /**
     * 계정 정지 해제 (LIMITED 롤 제거)
     * @param username 정지 해제할 사용자
     * @return 롤 변경 성공 여부
     */
    @DeleteMapping("/limited")
    public ApiResponse<Void> unlimitedRole(@RequestParam("username") String username) {
        var result = userService.removeRoleLimited(username);
        if (result) {
            return ApiResponse.ok(null);
        } else {
            return ApiResponse.status(HttpStatus.CONFLICT);
        }
    }

    /**
     * 인증 배지 부여 (BLUE_CHECK로 롤 변경)
     * @param username 대상자
     * @return 롤 변경 성공 여부
     */
    @PostMapping("/bluecheck")
    public ApiResponse<Void> blueCheckRole(@RequestParam("username") String username) {
        var result = userService.addRoleBlueCheck(username);
        if (result) {
            return ApiResponse.ok(null);
        } else {
            return ApiResponse.status(HttpStatus.CONFLICT);
        }
    }

    /**
     * 인증 배지 해제 (BLUE_CHECK 롤 제거)
     * @param username 대상자
     * @return 롤 변경 성공 여부
     */
    @DeleteMapping("/bluecheck")
    public ApiResponse<Void> unBlueCheckRole(@RequestParam("username") String username) {
        var result = userService.removeRoleBlueCheck(username);
        if (result) {
            return ApiResponse.ok(null);
        } else {
            return ApiResponse.status(HttpStatus.CONFLICT);
        }
    }
}