package like.lion.way.user.service.serviceImpl;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.domain.Follow;
import like.lion.way.user.domain.User;
import like.lion.way.user.dto.FollowDto;
import like.lion.way.user.repository.FollowRepository;
import like.lion.way.user.service.FollowService;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final FollowRepository followRepository;

    @Override
    public List<FollowDto> getFollowerList(HttpServletRequest request) {
        String token  = jwtUtil.getCookieValue(request,"accessToken");
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findByUserId(userId);
        System.out.println("userid:"+userId);
        List<Follow> followers = followRepository.findAllByFollower(user);
        System.out.println("followers: "+followers);
        List<FollowDto> followDtos = new ArrayList<>();
        for(Follow follow : followers){
            User followUser = follow.getFollowing();
            FollowDto followDto = new FollowDto();
            followDto.setUsername(followUser.getUsername());
//            followDto.setUserImgPath(followUser.getImgPath());
            followDtos.add(followDto);

        }
        return followDtos;
    }

    @Override
    public List<FollowDto> getFollowingList(HttpServletRequest request) {
        String token  = jwtUtil.getCookieValue(request,"accessToken");
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findByUserId(userId);

        List<Follow> followings = followRepository.findAllByFollowing(user);
        List<FollowDto> followingDtos = new ArrayList<>();
        for(Follow follow : followings){
            User followUser = follow.getFollower();
            FollowDto followDto = new FollowDto();
            followDto.setUsername(followUser.getUsername());
//            followDto.setUserImgPath(followUser.getImgPath());
            followingDtos.add(followDto);

        }
        return followingDtos;
    }
}
