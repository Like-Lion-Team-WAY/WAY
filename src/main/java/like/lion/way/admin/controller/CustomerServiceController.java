package like.lion.way.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerServiceController {
    //고객센터
    @GetMapping("/admin/customerService")
    public String customerService() {
        return "pages/admin/customerService";
    }
    //이용약관
    @GetMapping("/admin/termsOfuse")
    public String termsOfUse() {
        return "pages/admin/termsOfUse";
    }
    //개인정보처리방침
    @GetMapping("/admin/policyPersonalInfo")
    public String policyPersonalInfo() {
        return "pages/admin/policyPersonalInfo";
    }
}
