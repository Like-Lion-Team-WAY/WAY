package like.lion.way.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import like.lion.way.jwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 요청 헤더에서 JWT 토큰 추출
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            System.out.println(jwt);
            try {
                // 토큰에서 사용자 이름 추출
                username = jwtUtil.getUsernameFromAccessToken(jwt);
            } catch (Exception e) {
                // 토큰이 만료되었거나 유효하지 않음
                logger.warn("JWT token is either expired or invalid: " + e.getMessage());
            }
        }

        // 사용자 이름이 존재하고, 현재 컨텍스트에 인증 정보가 없는 경우
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateAccessToken(jwt)) {
                // 토큰이 유효한 경우, UserDetailsService를 사용하여 사용자 정보 로드
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                // 토큰이 유효하지 않거나 만료된 경우
                String refreshToken = request.getHeader("Refresh-Token");
                if (refreshToken != null && jwtUtil.validateRefreshToken(refreshToken)) {
                    // 리프레시 토큰이 유효한 경우
                    String newJwt = jwtUtil.generateAccessToken(username);

                    //addCookie ?? 해줘야하나 액세스토큰만?? (리프레스토큰을 또 발급하면 로그인 무한유지가능성)

                    response.setHeader("Authorization", "Bearer " + newJwt);
                    // 새로운 액세스 토큰을 발급하고 설정 후, UserDetailsService를 사용하여 사용자 정보 로드
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }

        chain.doFilter(request, response);
    }
}
