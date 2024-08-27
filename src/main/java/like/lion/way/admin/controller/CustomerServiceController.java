package like.lion.way.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/way")
public class CustomerServiceController {
    //고객센터
    @GetMapping("/customerService")
    public String customerService() {
        return "pages/admin/customerService";
    }
    //이용약관
    @GetMapping("/termsOfuse")
    public String termsOfUse() {
        return "pages/admin/termsOfUse";
    }
    //개인정보처리방침
    @GetMapping("/policyPersonalInfo")
    public String policyPersonalInfo() {
        return "pages/admin/policyPersonalInfo";
    }
    //청소년보호정책
    @GetMapping("/youthProtectionPolicy")
    public String youthProtectionPolicy() {
        return "pages/admin/youthProtectionPolicy";
    }
}
