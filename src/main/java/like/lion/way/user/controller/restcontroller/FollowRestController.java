package like.lion.way.user.controller.restcontroller;

import jakarta.servlet.http.HttpServletRequest;
import like.lion.way.user.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("follow")
public class FollowRestController {
    private final FollowService followService;

    @DeleteMapping("/deleteFollower")
    public ResponseEntity<?> deleteFollower(HttpServletRequest request, @RequestParam("username") String username){

        return followService.deleteFollower(request,username);
    }
    @DeleteMapping("/unFollowing")
    public ResponseEntity<?>unFollowing(HttpServletRequest request,@RequestParam("username")String username){
        return followService.unFollowing(request,username);
    }
    @PostMapping("/following")
    public ResponseEntity<?>following(HttpServletRequest request , @RequestParam("username") String username){
        return followService.following(request,username);
    }

    @GetMapping("/followCheck")
    public ResponseEntity<?> followCheck(HttpServletRequest request,@RequestParam("username")String username){
        return followService.followCheck(request,username);
    }
}
