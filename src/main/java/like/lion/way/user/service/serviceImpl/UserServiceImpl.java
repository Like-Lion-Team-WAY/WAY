package like.lion.way.user.service.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import like.lion.way.els.domain.ElsUser;
import like.lion.way.els.service.ElsUserService;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.domain.Interest;
import like.lion.way.user.domain.Role;
import like.lion.way.user.domain.User;
import like.lion.way.user.dto.SettingLoginInfoDto;
import like.lion.way.user.dto.UserProfileDto;
import like.lion.way.user.oauth2.dto.OAuthAttributes;
import like.lion.way.user.repository.UserRepository;
import like.lion.way.user.service.InterestService;
import like.lion.way.user.service.RoleService;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RoleService roleService;
    private final InterestService interestService;
    private final ElsUserService elsUserService;


    @Value("${image.upload.dir}")
    private String uploadDir;

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
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(Math.toIntExact(jwtUtil.ACCESS_TOKEN_EXPIRE_COUNT / 1000));

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(Math.toIntExact(jwtUtil.REFRESH_TOKEN_EXPIRE_COUNT / 1000));


        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }

    @Override
    public void deleteUser(Long userId) {
        //elasticSearch 에서도 지워지게
        elsUserService.deleteByUserId(userId.toString());
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
        User user = getUserByToken(request);

        user.setUsername(loginInfoDto.getUsername());
        user.setNickname(loginInfoDto.getNickname());

        ElsUser elsUser= elsUserService.findByUserId(user.getUserId());
        if(elsUser != null) {
            elsUser.setUsername(user.getUsername());
            elsUser.setImageUrl(user.getUserImage());
            elsUserService.saveOrUpdate(elsUser);
        }
        addCookies(response, user); //추가된 코드
        return saveOrUpdateUser(user);
    }
    @Override
    public User saveOrUpdateUser(User user){
        return userRepository.save(user);

    }

    @Override
    public User addInterests(HttpServletRequest request, HttpServletResponse response, Set<String> interests) {

        User user = getUserByToken(request);

        Set<Interest> set = new HashSet<>();
        for(String str  : interests){
            Interest interest = interestService.findOrSaveInterest(str);
            set.add(interest);
        }
        user.setInterests(set);

        ElsUser elsUser = new ElsUser();
        elsUser.setId(user.getUserId().toString());
        elsUser.setUsername(user.getUsername());
        elsUser.setImageUrl(user.getUserImage());

        List<String> interestNames = set.stream()
                .map(Interest::getInterestName)
                .collect(Collectors.toList()); //관심사 태그들

        interestNames.add(user.getUsername()); //유저 이름도 넣어줌

        elsUser.setInterests(interestNames);

        for (String interestName : interestNames) {
            System.out.println(interestName);
        }

        elsUserService.saveOrUpdate(elsUser);

        return userRepository.save(user);
    }

    @Override
    public UserProfileDto getProfile(HttpServletRequest request) throws NullPointerException{
        User user = getUserByToken(request);
        UserProfileDto dto = new UserProfileDto();
        dto.setUserImage(user.getUserImage());
        dto.setNickname(user.getNickname());
        dto.setUsername(user.getUsername());
        dto.setInterests(user.getInterests());
        dto.setUserImage(user.getUserImage());

        return dto;
    }

    @Override
    public User updateUserInfo(SettingLoginInfoDto updateUserDto, HttpServletRequest request) {
        User user = getUserByToken(request);

        user.setUsername(updateUserDto.getUsername());
        user.setNickname(updateUserDto.getNickname());

        return saveOrUpdateUser(user);
    }

    @Override
    public ResponseEntity<String> updateOrSaveImg( String deleteFileName , HttpServletRequest request , String key) {
        User user = getUserByToken(request);
        user.setUserImage(key);
        saveOrUpdateUser(user);
        return ResponseEntity.ok(key);
    }

    @Override
    public User getUserByToken(HttpServletRequest request){
        String token = jwtUtil.getCookieValue(request, "accessToken");
        Long userId = jwtUtil.getUserIdFromToken(token);
        return findByUserId(userId);
    }

    @Override
    public void logout(HttpServletResponse response) {


        Cookie accessToken = new Cookie("accessToken" , null);
        accessToken.setMaxAge(0);
        accessToken.setPath("/");

        Cookie refreshToken = new Cookie("refreshToken",null);
        refreshToken.setMaxAge(0);
        refreshToken.setPath("/");

        response.addCookie(refreshToken);
        response.addCookie(accessToken);
    }

    @Override
    public String getUserImagePath(HttpServletRequest request) {
        String token = jwtUtil.getCookieValue(request, "accessToken");
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userRepository.findById(userId).orElse(null);
        return user.getUserImage();
    }
}
