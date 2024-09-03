package like.lion.way.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import like.lion.way.feed.util.UserUtil;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CustomerServiceController {
    private final UserService userService;
    private final UserUtil userUtil;

    //고객센터
    @GetMapping("/way/customerService")
    public String customerService() {
        return "pages/admin/customerService";
    }

    //이용약관
    @GetMapping("/way/termsOfuse")
    public String termsOfUse() {
        return "pages/admin/termsOfUse";
    }

    //개인정보처리방침
    @GetMapping("/way/policyPersonalInfo")
    public String policyPersonalInfo() {
        return "pages/admin/policyPersonalInfo";
    }

    //청소년보호정책
    @GetMapping("/way/youthProtectionPolicy")
    public String youthProtectionPolicy() {
        return "pages/admin/youthProtectionPolicy";
    }

    //블루체크 뱃지 신청창
    @GetMapping("/blueCheck/application")
    public String blueCheck(HttpServletRequest request, Model model) {

        User user = userUtil.getLoginUser(request);

        if (user == null) {
            return "redirect:/user/login";
        } else {
            //이미 신청한 사용자
            if (user.getRoles().stream().anyMatch(role -> role.getRoleId().equals(4L))) {
                model.addAttribute("userId", null);
            }
            //신청 안 한 사용자
            else {
                Long userId = user.getUserId();
                model.addAttribute("userId", userId);
            }
        }
        return "pages/admin/bluecheckApplication";
    }
}
