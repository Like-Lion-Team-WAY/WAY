package like.lion.way.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.domain.User;
import like.lion.way.user.dto.SettingLoginInfoDto;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/login")
    public String testView(){
        return "pages/user/login";
    }

    @GetMapping("/loginInfo")
    public String loginInfoView(){
        return "pages/user/loginInfo";
    }
    @PostMapping("/loginInfo")
    public String loginInfo(@ModelAttribute SettingLoginInfoDto loginInfoDto,
                            HttpServletRequest request){
        User user = userService.updateLoginInfo(loginInfoDto,request);
        System.out.println(user);
        if(user!=null){
            return "redirect:/user/like";
        }else{
            return "redirect:/user/loginInfo";
        }
    }
    @GetMapping("/like")
    public String likeView(){
        return "pages/user/like";
    }
}
