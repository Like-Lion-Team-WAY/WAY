package like.lion.way.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import like.lion.way.config.SecurityConfig;
import like.lion.way.jwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

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
        Long userId = null;

        // 허용된 경로 확인
        String requestURI = new UrlPathHelper().getPathWithinApplication(request);
        RequestMatcher permitAllMatcher = new AntPathRequestMatcher(requestURI);
        if (SecurityConfig.PERMIT_ALL_PATHS.stream().anyMatch(path -> permitAllMatcher.matches(request))) {
            chain.doFilter(request, response);
            return;
        }

        System.out.println("필터탔어요");
        // 요청 헤더에서 JWT 토큰 추출
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            System.out.println(jwt);
            try {
                // 토큰에서 사용자 이름 추출
                username = jwtUtil.getUserNameFromToken(jwt);
                userId = jwtUtil.getUserIdFromToken(jwt);
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
                    if(jwtUtil.validateRefreshToken(refreshToken)){
                        response.setHeader("Authorization", "Bearer " + refreshToken);
                        // 새로운 액세스 토큰을 발급하고 설정 후, UserDetailsService를 사용하여 사용자 정보 로드
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }
        }

        chain.doFilter(request, response);
    }
}
