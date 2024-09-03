package like.lion.way.admin.controller;

import java.util.Comparator;
import like.lion.way.admin.domain.BlueCheck;
import like.lion.way.admin.service.BlueCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final BlueCheckService blueCheckService;

    @GetMapping("/super")
    public String admin() {
        return "redirect:/super/report";
    }

    @GetMapping("/super/report")
    public String report() {
        return "pages/admin/report";
    }

    @GetMapping("/super/bluecheck")
    public String blueCheckApplication(Model model) {
        model.addAttribute("bluecheck", blueCheckService.findAll()
                .stream()
                .sorted(Comparator.comparing(BlueCheck::getBlueCheckDate))
                .toList());
        return "pages/admin/blueCheckList";
    }
}
