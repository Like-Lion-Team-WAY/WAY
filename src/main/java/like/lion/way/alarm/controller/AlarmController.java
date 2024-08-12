package like.lion.way.alarm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AlarmController {
    @GetMapping("/alarm")
    public String showAlarmList() {
        return "/pages/alarm/alarm";
    }

    @GetMapping("/alarm/setting")
    public String showAlarmSetting() {
        return "/pages/alarm/alarmSetting";
    }
}
