package like.lion.way.user.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import like.lion.way.user.dto.FollowDto;

public interface FollowService {
    List<FollowDto> getFollowerList(HttpServletRequest request);

    List<FollowDto> getFollowingList(HttpServletRequest request);
}
