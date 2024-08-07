package like.lion.way.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import like.lion.way.user.dto.FollowDto;
import like.lion.way.user.service.BlockService;
import like.lion.way.user.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;
    private final BlockService blockService;
    @GetMapping("/followSetting")
    public String followSetting(HttpServletRequest request, HttpServletResponse response, Model model){
        List<FollowDto> followerList =  followService.getFollowerList(request);
        List<FollowDto> followingList = followService.getFollowingList(request);
        List<String> blockList = blockService.getBlcokList(request);
        model.addAttribute("follower", followerList);
        model.addAttribute("following",followingList);
        model.addAttribute("block",blockList);
        return "pages/user/followSetting";
    }

}
