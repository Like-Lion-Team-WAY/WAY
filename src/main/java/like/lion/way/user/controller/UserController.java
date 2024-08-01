package like.lion.way.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/user/login")
    public String testView(){
        return "pages/user/login";
    }
    @GetMapping("/main")
    public String mainView(){
        return "pages/common/main";
    }
    @GetMapping("/user/loginInfo")
    public String loginInfoView(){
        return "pages/user/loginInfo";
    }
}
