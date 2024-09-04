package like.lion.way.admin.controller;

import java.util.Comparator;
import like.lion.way.admin.domain.BlueCheck;
import like.lion.way.admin.service.BlueCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/super")
public class AdminController {
    private final BlueCheckService blueCheckService;

    /**
     * 관리자 페이지로 이동
     */
    @GetMapping()
    public String admin() {
        return "redirect:/super/report";
    }

    /**
     * 신고 목록 관리자 페이지로 이동
     */
    @GetMapping("/report")
    public String report() {
        return "pages/admin/report";
    }

    /**
     * 인증 배지 신청 목록 관리자 페이지로 이동
     * @param model 인증 배지 신청 목록
     */
    @GetMapping("/bluecheck")
    public String blueCheckApplication(Model model) {
        model.addAttribute("bluecheck", blueCheckService.findAll()
                .stream()
                .sorted(Comparator.comparing(BlueCheck::getBlueCheckDate))
                .toList());
        return "pages/admin/blueCheckList";
    }
}
