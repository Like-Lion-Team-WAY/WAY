package like.lion.way.user.service.serviceImpl;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import like.lion.way.user.domain.Follow;
import like.lion.way.user.domain.User;
import like.lion.way.user.dto.FollowDto;
import like.lion.way.user.repository.FollowRepository;
import like.lion.way.user.service.FollowService;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final UserService userService;
    private final FollowRepository followRepository;

    /**
     * 팔로워 리스트
     * @param user user객체의 팔로워
     */
    @Override
    public List<FollowDto> getFollowerList(User user) {
        List<Follow> followers = followRepository.findAllByFollower(user);
        List<FollowDto> followDtos = new ArrayList<>();
        for(Follow follow : followers){
            User followUser = follow.getFollowing();
            FollowDto followDto = new FollowDto();
            followDto.setUsername(followUser.getUsername());
            followDto.setUserImgPath(followUser.getUserImage());
            followDtos.add(followDto);
        }
        return followDtos;
    }

    /**
     * 팔로잉 리스트
     * @param user user객체의 팔로잉
     */
    @Override
    public List<FollowDto> getFollowingList(User user) {
        List<Follow> followings = followRepository.findAllByFollowing(user);
        List<FollowDto> followingDtos = new ArrayList<>();
        for(Follow follow : followings){
            User followUser = follow.getFollower();
            FollowDto followDto = new FollowDto();
            followDto.setUsername(followUser.getUsername());
            followDto.setUserImgPath(followUser.getUserImage());
            followingDtos.add(followDto);

        }
        return followingDtos;
    }

    /**
     * 팔로워 삭제
     * @param username 삭제당하는 username
     */
    @Transactional
    @Override
    public ResponseEntity<?> deleteFollower(HttpServletRequest request, String username) {
        User deleteUser = userService.getUserByToken(request);
        User deletedUser = userService.findByUsername(username);

        followRepository.deleteByFollowerAndFollowing(deleteUser,deletedUser);
        return ResponseEntity.ok("success");
    }

    /**
     * 언팔로잉
     * @param username 언팔로잉 당하는 user
     */
    @Transactional
    @Override
    public ResponseEntity<?> unFollowing(HttpServletRequest request, String username) {
        User user = userService.getUserByToken(request);
        User unfollowingUser = userService.findByUsername(username);
        followRepository.deleteByFollowerAndFollowing(unfollowingUser,user);
        return ResponseEntity.ok("success");
    }

    /**
     * 팔로잉
     * @param username 팔로잉 당하는 user
     */
    @Transactional
    @Override
    public ResponseEntity<?> following(HttpServletRequest request, String username) {

        User following = userService.getUserByToken(request);
        User followed = userService.findByUsername(username);
        Follow follow = new Follow();
        follow.setFollower(followed);
        follow.setFollowing(following);
        Follow result = followRepository.save(follow);
        if(result.getFollowId()!=null){
            return ResponseEntity.ok("success");
        }else{
            return ResponseEntity.ofNullable("null");
        }
    }

    /**
     * 팔로우 여부 체크
     * @param username 현재 피드유저
     */
    @Override
    public ResponseEntity<?> followCheck(HttpServletRequest request, String username) {
        User nowUser = userService.getUserByToken(request);
        User feedUser = userService.findByUsername(username);
        if(followRepository.findByFollowerAndFollowing(feedUser, nowUser)!=null){
            return ResponseEntity.ok("following");
        }else{
            return ResponseEntity.ok("notFollowing");
        }

    }
}
