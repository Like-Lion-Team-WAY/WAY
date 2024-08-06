package like.lion.way.user.service.serviceImpl;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.domain.Block;
import like.lion.way.user.domain.User;
import like.lion.way.user.repository.BlockRepository;
import like.lion.way.user.service.BlockService;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlockServiceImpl implements BlockService {
    private final BlockRepository blockRepository;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    @Override
    public List<String> getBlcokList(HttpServletRequest request) {

        String token  = jwtUtil.getCookieValue(request,"accessToken");
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findByUserId(userId);
        List<Block> blocks = blockRepository.findAllByBlockerUserId(user);
        List<String> blockedNames = new ArrayList<>();
        for(Block block:blocks){
            User blockedUser = block.getBlockedUserId();
            blockedNames.add(blockedUser.getUsername());
        }
        return blockedNames;
    }
}
