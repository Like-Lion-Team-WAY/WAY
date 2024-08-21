package like.lion.way.feed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShareMypage {
    @GetMapping("/share")
    public String shareMypage() {
        return "pages/feed/shareMypage";
    }
}
