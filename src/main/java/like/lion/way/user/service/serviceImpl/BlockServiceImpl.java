package like.lion.way.user.service.serviceImpl;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import like.lion.way.feed.domain.Post;
import like.lion.way.feed.domain.Question;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.domain.Block;
import like.lion.way.user.domain.User;
import like.lion.way.user.repository.BlockRepository;
import like.lion.way.user.service.BlockService;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlockServiceImpl implements BlockService {
    private final BlockRepository blockRepository;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    @Override
    public List<String> getBlcokList(User user) {
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

    @Override
    public List<?> blockFilter(List<?> checkContents, HttpServletRequest request) {
        String token = jwtUtil.getCookieValue(request, "accessToken");
        Long userId = jwtUtil.getUserIdFromToken(token);
        if(userId==null){
            return checkContents;
        }
        User user = userService.findByUserId(userId);
        List<Block> blocks = blockRepository.findAllByBlockerUserId(user);
        List<Object> list = new ArrayList<>();
        for (Object content : checkContents) {
            //질문관련 차단
            if (content instanceof Question) {
                try {
                    // 먼저 getQuestioner()가 null인지 확인
                    User questioner = ((Question) content).getQuestioner();

                    if (questioner == null) {
                        list.add(content);
                        continue;
                    }

                    Long questionId = questioner.getUserId();

                    boolean isBlocked = false;
                    for (Block block : blocks) {
                        if (block.getBlockedUserId().getUserId().equals(questionId)) {
                            isBlocked = true;
                            break;
                        }
                    }

                    if (!isBlocked) {
                        list.add(content);
                    }

                } catch (Exception e) {
                    log.error("Error while filtering content: {}", e.getMessage(), e);
                }
            }
            //post관련 차단
            else if(content instanceof Post){
                try{
                    Long postUserId = ((Post) content).getUser().getUserId();
                    boolean isBlocked = false;
                    for (Block block : blocks) {
                        if (block.getBlockedUserId().getUserId().equals(postUserId)) {
                            isBlocked = true;
                            break;
                        }
                    }
                    if (!isBlocked) {
                        list.add(content);
                    }
                }catch(Exception e){
                    log.error("Error while filtering content: {}" , e.getMessage());
                }
            }
        }
        return list;
    }

    @Override
    public Block findByUser(User feedUser, HttpServletRequest request) {
        String token = jwtUtil.getCookieValue(request, "accessToken");
        Long userId = jwtUtil.getUserIdFromToken(token);
        User nowUser = userService.findByUserId(userId);
        return blockRepository.findByBlockerUserIdAndBlockedUserId(nowUser,feedUser);
    }
}
