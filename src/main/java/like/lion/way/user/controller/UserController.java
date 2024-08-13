package like.lion.way.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.domain.User;
import like.lion.way.user.dto.SettingLoginInfoDto;
import like.lion.way.user.dto.UserProfileDto;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
                            HttpServletRequest request, HttpServletResponse response){
        User user = userService.updateLoginInfo(loginInfoDto,request, response);
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

    @GetMapping("/setting")
    public ModelAndView settingView(HttpServletRequest request , ModelAndView model){
        UserProfileDto dto = userService.getProfile(request);
        model.addObject("profile" , dto);
        model.setViewName("pages/user/profileSetting");
        return model;
    }

    @PostMapping("/updateUserInfo")
    public String updateUserInfo(@ModelAttribute SettingLoginInfoDto updateUserDto , HttpServletRequest request,HttpServletResponse response){
        User user = userService.updateLoginInfo(updateUserDto,request, response);

        if(user!=null){
            return "redirect:/main";
        }else{
            return "redirect:/user/setting";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response){
        userService.logout(response);
        return "redirect:/user/login";
    }

}
