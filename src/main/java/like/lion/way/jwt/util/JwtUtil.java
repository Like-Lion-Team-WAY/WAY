package like.lion.way.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {
    private final byte[] accessSecret;
    private final byte[] refreshSecret;

    public static Long ACCESS_TOKEN_EXPIRE_COUNT = 2 * 60 * 60 * 1000L; // 2시간
    public static Long REFRESH_TOKEN_EXPIRE_COUNT = 7 * 24 * 60 * 60 * 1000L; // 7일

    public JwtUtil(@Value("${jwt.secretKey}") String accessSecret, @Value("${jwt.refreshKey}") String refreshSecret) {
        this.accessSecret = accessSecret.getBytes(StandardCharsets.UTF_8);
        this.refreshSecret = refreshSecret.getBytes(StandardCharsets.UTF_8);
    }

    //token create
    private String createToken(Long id , String email  , String username , List<String> roles , Long expire , byte[] secretKey){
        Claims claims = Jwts.claims().setSubject(email);

        claims.put("roles" , roles);
        claims.put("userId",id);
        claims.put("username" , username);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expire))
                .signWith(getSigningKey(secretKey))
                .compact();
    }
    public static Key getSigningKey(byte[] secretKey){
        return Keys.hmacShaKeyFor(secretKey);
    }

    //AccessToken생성
    public String createAccessToken(Long id , String email , String username , List<String> roles){
        return createToken(id,email , username ,roles,ACCESS_TOKEN_EXPIRE_COUNT,accessSecret);
    }
    //RefreshToken생성
    public String createRefreshToken(Long id , String email , String username , List<String> roles){
        return createToken(id,email,username ,roles,REFRESH_TOKEN_EXPIRE_COUNT,refreshSecret);
    }

    public Long getUserIdFromToken(String token){
        String[] tokenArr = token.split(" ");
        if (tokenArr.length > 1) {
            token = tokenArr[1];
        }
        Claims claims = parseToken(token, accessSecret);
        return Long.valueOf((Integer) claims.get("userId"));
    }
    public String getUserNameFromToken(String token){
        String[] tokenArr = token.split(" ");
        if (tokenArr.length > 1) {
            token = tokenArr[1];
        }
        Claims claims = parseToken(token, accessSecret);
        return (String) claims.get("username");
    }

    public Claims parseToken(String token, byte[] secretKey){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public Claims parseAccessToken(String accessToken) {
        return parseToken(accessToken, accessSecret);
    }

    public Claims parseRefreshToken(String refreshToken) {
        return parseToken(refreshToken, refreshSecret);
    }


    // Token 유효성 검증
    public boolean validateToken(String token, byte[] secretKey) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey(secretKey))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token expired", e);
        } catch (Exception e) {
            log.error("Token invalid", e);
        }
        return false;
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, accessSecret);
    }
    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshSecret);
    }
    public String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
