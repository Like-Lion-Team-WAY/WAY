package like.lion.way.els.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class ElsUserController {
    @GetMapping("/searchform")
    public String showSearchPage() {
        return "pages/user/searchform";
    }
}
