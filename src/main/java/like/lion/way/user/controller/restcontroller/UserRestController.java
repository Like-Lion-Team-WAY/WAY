package like.lion.way.user.controller.restcontroller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping("/duplicate")
    public ResponseEntity<Boolean> duplicateCheck(@RequestParam("username") String username){
        User user =userService.findByUsername(username);
        if(user==null){
            return ResponseEntity.ok(true);
        }else{
            return ResponseEntity.ok(false);
        }
    }

    @PostMapping("/addInterests")
    public ResponseEntity<String> addInterests(HttpServletRequest request
                                                , HttpServletResponse response
                                                , @RequestBody Set<String> interests){
        User user = userService.addInterests(request,response,interests);
        if(user.getInterests()!=null){
            return ResponseEntity.ok("success");
        }
        return ResponseEntity.ok("fail");
    }
}
