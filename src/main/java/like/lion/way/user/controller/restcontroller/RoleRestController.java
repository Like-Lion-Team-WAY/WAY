package like.lion.way.user.controller.restcontroller;

import like.lion.way.ApiResponse;
import like.lion.way.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleRestController {
    @Autowired private UserService userService;

    @PostMapping("/api/role/limited")
    public ApiResponse<Void> limitedRole(@RequestParam("username") String username) {
        var result = userService.addRoleLimited(username);
        if (result) {
            return ApiResponse.ok(null);
        } else {
            return ApiResponse.status(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/api/role/limited")
    public ApiResponse<Void> unlimitedRole(@RequestParam("username") String username) {
        var result = userService.removeRoleLimited(username);
        if (result) {
            return ApiResponse.ok(null);
        } else {
            return ApiResponse.status(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/api/role/bluecheck")
    public ApiResponse<Void> blueCheckRole(@RequestParam("username") String username) {
        var result = userService.addRoleBlueCheck(username);
        if (result) {
            return ApiResponse.ok(null);
        } else {
            return ApiResponse.status(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/api/role/bluecheck")
    public ApiResponse<Void> unBlueCheckRole(@RequestParam("username") String username) {
        var result = userService.removeRoleBlueCheck(username);
        if (result) {
            return ApiResponse.ok(null);
        } else {
            return ApiResponse.status(HttpStatus.CONFLICT);
        }
    }
}