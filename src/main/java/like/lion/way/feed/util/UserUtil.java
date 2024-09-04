package like.lion.way.feed.util;

import jakarta.servlet.http.HttpServletRequest;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class  UserUtil {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public User getLoginUser(HttpServletRequest request) {

        String token = jwtUtil.getCookieValue(request, "accessToken");

        if (token == null || token.isEmpty()) {
            log.info("Token is null or empty");
            return null;
        }

        Long loginId = jwtUtil.getUserIdFromToken(token);

        if (loginId == null) {
            log.info("Login ID is null");
            return null;
        }

        return userService.findByUserId(loginId);
    }
}
