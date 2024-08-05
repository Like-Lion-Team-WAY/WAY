package like.lion.way.alarm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AlarmController {
    @GetMapping("/alarms")
    public String showAlarmList() {
        return "/pages/alarm/alarm";
    }
}
