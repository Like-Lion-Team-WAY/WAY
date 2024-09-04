package like.lion.way.alarm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/alarm")
public class AlarmController {
    /**
     * 알림 목록 페이지로 이동
     */
    @GetMapping()
    public String showAlarmList() {
        return "pages/alarm/alarm";
    }

    /**
     * 알림 설정 페이지로 이동
     */
    @GetMapping("/setting")
    public String showAlarmSetting() {
        return "pages/alarm/alarmSetting";
    }
}
