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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public ResponseEntity<?> unblock(HttpServletRequest request, String username) {
        String token  = jwtUtil.getCookieValue(request,"accessToken");
        Long userId = jwtUtil.getUserIdFromToken(token);
        User blockedUser = userService.findByUserId(userId);
        User unblockedUser = userService.findByUsername(username);
        blockRepository.deleteByBlockerUserIdAndBlockedUserId(blockedUser,unblockedUser);
        return ResponseEntity.ok("success");
    }
}
