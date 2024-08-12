package like.lion.way.user.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import like.lion.way.user.domain.User;
import like.lion.way.user.dto.FollowDto;
import org.springframework.http.ResponseEntity;

public interface FollowService {
    List<FollowDto> getFollowerList(User user);

    List<FollowDto> getFollowingList(User user);

    ResponseEntity<?> deleteFollower(HttpServletRequest request, String username);

    ResponseEntity<?> unFollowing(HttpServletRequest request, String username);

    ResponseEntity<?> following(HttpServletRequest request, String username);

    ResponseEntity<?> followCheck(HttpServletRequest request, String username);
}
