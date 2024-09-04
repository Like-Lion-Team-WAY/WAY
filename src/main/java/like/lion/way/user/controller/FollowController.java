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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final BlockService blockService;
    private final UserService userService;

    /**
     * 팔로우 팔로잉 차단 페이지 이동(본인)
     */
    @GetMapping("/followSetting")
    public String followSetting(HttpServletRequest request,Model model){
        User user = userService.getUserByToken(request);
        List<FollowDto> followerList =  followService.getFollowerList(user);
        List<FollowDto> followingList = followService.getFollowingList(user);
        List<String> blockList = blockService.getBlcokList(user);
        model.addAttribute("follower", followerList);
        model.addAttribute("following",followingList);
        model.addAttribute("block",blockList);
        model.addAttribute("another","itsme");
        return "pages/user/followSetting";
    }

    /**
     * 팔로우 팔로잉 페이지 이동(타유저)
     * @param userId
     */
    @GetMapping("/followList")
    public String followList(@RequestParam("userId")Long userId , Model model , HttpServletRequest request){
        User nowUser = userService.getUserByToken(request);
        User user = userService.findByUserId(userId);
        if(nowUser.getUserId()==user.getUserId()){
            return "redirect:/user/followSetting";
        }
        List<FollowDto> followerList =  followService.getFollowerList(user);
        List<FollowDto> followingList = followService.getFollowingList(user);
        model.addAttribute("follower", followerList);
        model.addAttribute("following",followingList);
        model.addAttribute("block",null);
        model.addAttribute("another", "another");
        return "pages/user/followSetting";
    }

}
