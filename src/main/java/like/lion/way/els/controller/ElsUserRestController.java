package like.lion.way.els.controller;

import java.util.List;
import like.lion.way.ApiResponse;
import like.lion.way.els.domain.ElsUser;
import like.lion.way.els.service.ElsUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class ElsUserRestController {
    private final ElsUserService elsUserService;

    /**
     * 모든 사용자 조회
     */
    @GetMapping("/all")
    public ApiResponse<List<ElsUser>> getAllUsers() {
        List<ElsUser> users = elsUserService.getAllUsers();
        return ApiResponse.ok(users);
    }

    /**
     * 사용자 검색 (관심사 + username + nickname)
     * @param username
     */
    @GetMapping("/search")
    public ApiResponse<List<ElsUser>> searchUsers(@RequestParam String username) {
        List<ElsUser> users = elsUserService.searchUsersByInterest(username);
        return ApiResponse.ok(users);
    }
}
