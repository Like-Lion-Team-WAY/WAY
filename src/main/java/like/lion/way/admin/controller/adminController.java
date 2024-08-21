package like.lion.way.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class adminController {
    @GetMapping("/admin/report")
    public String report() {
        return "/pages/admin/report";
    }
}
