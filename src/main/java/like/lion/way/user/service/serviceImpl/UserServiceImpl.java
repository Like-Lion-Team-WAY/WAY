package like.lion.way.user.service.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.domain.Role;
import like.lion.way.user.domain.RoleType;
import like.lion.way.user.domain.User;
import like.lion.way.user.dto.SettingLoginInfoDto;
import like.lion.way.user.oauth2.dto.OAuthAttributes;
import like.lion.way.user.repository.UserRepository;
import like.lion.way.user.service.RoleService;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RoleService roleService;

    @Override
    public User findByUserId(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail()).orElse(new User());

//        user.setUsername(attributes.getName());
        user.setProvider(attributes.getProvider());
        user.setCreatedAt(LocalDate.now());
        user.setEmail(attributes.getEmail());

        Set<Role> set = new HashSet<>();
        Role role =roleService.findByRoleName("USER");
        set.add(role);
        user.setRoles(set);

        user.initializeAlarmSetting();

        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(new User());

    }

    @Override
    public void addCookies(HttpServletResponse response, User user) {
        String accessToken = jwtUtil.createAccessToken(
                user.getUserId(),
                user.getEmail(),
                user.getUsername(),
                user.getRoles().stream()
                        .map(Role::getRoleName)
                        .collect(Collectors.toList())
        );
        String refreshToken = jwtUtil.createRefreshToken(
                user.getUserId(),
                user.getEmail(),
                user.getUsername(),
                user.getRoles().stream()
                        .map(Role::getRoleName)
                        .collect(Collectors.toList())
        );
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(Math.toIntExact(jwtUtil.ACCESS_TOKEN_EXPIRE_COUNT / 1000));

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(Math.toIntExact(jwtUtil.REFRESH_TOKEN_EXPIRE_COUNT / 1000));


        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);

    }

    @Override
    public void deleteCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("username" , null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        Cookie accessToken = new Cookie("accessToken" , null);
        accessToken.setMaxAge(0);
        accessToken.setPath("/");

        Cookie refreshToken = new Cookie("refreshToken",null);
        refreshToken.setMaxAge(0);
        refreshToken.setPath("/");

        response.addCookie(refreshToken);
        response.addCookie(cookie);
        response.addCookie(accessToken);
    }

    @Override
    public User updateLoginInfo(SettingLoginInfoDto loginInfoDto, HttpServletRequest request, HttpServletResponse response) {
        String token = jwtUtil.getCookieValue(request,"accessToken");
        Long userId = jwtUtil.getUserIdFromToken(token);
        String username = jwtUtil.getUserNameFromToken(token);
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found")) ;
        user.setUsername(loginInfoDto.getUsername());
        user.setNickname(loginInfoDto.getNickname());
        addCookies(response, user); //추가된 코드
        return saveOrUpdateUser(user);
    }
    @Override
    public User saveOrUpdateUser(User user){
        return userRepository.save(user);

    }
}
