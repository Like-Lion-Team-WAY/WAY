package like.lion.way.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/test")
    public String testView(){
        return "pages/user/test";
    }
}
