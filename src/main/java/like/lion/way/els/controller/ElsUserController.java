package like.lion.way.els.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class ElsUserController {
    /**
     * 사용자 검색 페이지로 이동
     */
    @GetMapping("/searchform")
    public String showSearchPage() {
        return "pages/user/searchform";
    }
}
