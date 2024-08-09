package like.lion.way.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import like.lion.way.user.domain.User;
import like.lion.way.user.dto.FollowDto;
import like.lion.way.user.service.BlockService;
import like.lion.way.user.service.FollowService;
import like.lion.way.user.service.UserService;
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
    private final UserService userService;
    @GetMapping("/followSetting")
    public String followSetting(HttpServletRequest request,Model model){
        User user = userService.getUserByToken(request);
        List<FollowDto> followerList =  followService.getFollowerList(user);
        List<FollowDto> followingList = followService.getFollowingList(user);
        List<String> blockList = blockService.getBlcokList(request);
        model.addAttribute("follower", followerList);
        model.addAttribute("following",followingList);
        model.addAttribute("block",blockList);
        return "pages/user/followSetting";
    }

}
