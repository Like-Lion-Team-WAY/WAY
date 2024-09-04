package like.lion.way.user.service.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
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
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * userId로 유저찾기
     * @param userId 유저아이디
     */
    @Override
    public User findByUserId(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    /**
     * username으로 유저찾기
     * @param username 유저네임
     */
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 회원가입(social login save)
     * @param attributes 소셜로그인한 사용자 정보
     */
    @Transactional
    @Override
    public User saveOrUpdate(OAuthAttributes attributes) {
        var optionalUser = userRepository.findByEmail(attributes.getEmail());
        User user;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            user = new User();
            Set<Role> set = new HashSet<>();
            set.add(roleService.findByRoleName("ROLE_USER"));
            user.setRoles(set);
            user.setCreatedAt(LocalDate.now());
        }

        user.setProvider(attributes.getProvider());
        user.setEmail(attributes.getEmail());

        user.initializeAlarmSetting();
        user.initializeChattingAlarm();

        return userRepository.save(user);
    }

    /**
     *  email로 user찾기
     * @param email 이메일
     */
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(new User());

    }

    /**
     *  AccessToken , RefreshToken 발급
     * @param user 토큰발급할 user 객체
     */
    @Transactional
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

    /**
     * 회원탈퇴
     * @param userId 유저아이디
     */
    @Transactional
    @Override
    public void deleteUser(Long userId) {
        //elasticSearch 에서도 지워지게
        elsUserService.deleteByUserId(userId.toString());
        userRepository.deleteById(userId);

    }

    /**
     * 로그아웃 시 쿠키삭제
     */
    @Transactional
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

    /**
     * 회원가입 시 추가정보입력(username , nickname) 저장
     * @param loginInfoDto username , nickname 있음
     */
    @Transactional
    @Override
    public User updateLoginInfo(SettingLoginInfoDto loginInfoDto, HttpServletRequest request, HttpServletResponse response) {
        User user = getUserByToken(request);

        user.setUsername(loginInfoDto.getUsername());
        user.setNickname(loginInfoDto.getNickname());


        ElsUser elsUser= elsUserService.findByUserId(user.getUserId());
        if(elsUser != null) {
            elsUser.setNickname(user.getNickname());
            elsUser.setUsername(user.getUsername());
            elsUser.setImageUrl(user.getUserImage());
            elsUserService.saveOrUpdate(elsUser);
        }
        addCookies(response, user);
      
        return saveOrUpdateUser(user);
    }

    /**
     *  회원정보 수정 , 생성
     * @param user 수정 , 생성하기위한 User객체
     */
    @Transactional
    @Override
    public User saveOrUpdateUser(User user){
        return userRepository.save(user);

    }

    /**
     *  관심 생성
     * @param interests 설정한 관심이름들
     */
    @Transactional
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
        elsUser.setNickname(user.getNickname());
        elsUser.setImageUrl(user.getUserImage());
        elsUser.setUsername(user.getUsername());

        List<String> interestNames = set.stream()
                .map(Interest::getInterestName)
                .collect(Collectors.toList()); //관심사 태그들

        interestNames.add(user.getNickname()); //유저 닉네임도 넣어줌
        interestNames.add(user.getUsername()); //유저 이름도 넣어줌

        elsUser.setInterests(interestNames);

        elsUserService.saveOrUpdate(elsUser);

        return userRepository.save(user);
    }

    /**
     * 내프로필 설정 프로필 가져오기
     */
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

    /**
     * 회원 프로필 이미지 저장 , 수정
     * @param deleteFileName 삭제한 파일경로+파일이름
     * @param key s3에 올라간 파일경로+파일이름
     */
    @Transactional
    @Override
    public ResponseEntity<String> updateOrSaveImg( String deleteFileName , HttpServletRequest request , String key) {
        User user = getUserByToken(request);
        user.setUserImage(key);
        saveOrUpdateUser(user);
        return ResponseEntity.ok(key);
    }

    /**
     * 유저에게서 토큰 추출
     */
    @Override
    public User getUserByToken(HttpServletRequest request){
        String token = jwtUtil.getCookieValue(request, "accessToken");
        Long userId = jwtUtil.getUserIdFromToken(token);
        return findByUserId(userId);
    }

    /**
     * 로그아웃(토큰뺏기)
     */
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
    @Transactional
    public boolean addRoleLimited(String username) {
        return addRole(username, roleService.findByRoleName("ROLE_LIMITED"));
    }

    @Override
    @Transactional
    public boolean removeRoleLimited(String username) {
        return removeRole(username, roleService.findByRoleName("ROLE_LIMITED"));
    }

    @Override
    @Transactional
    public boolean addRoleBlueCheck(String username) {
        return addRole(username, roleService.findByRoleName("ROLE_BLUECHECK"));
    }

    @Override
    @Transactional
    public boolean removeRoleBlueCheck(String username) {
        return removeRole(username, roleService.findByRoleName("ROLE_BLUECHECK"));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isBlueCheck(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new EntityNotFoundException("User not found");
        return user.getRoles().contains(roleService.findByRoleName("ROLE_BLUECHECK"));
    }

    @Transactional
    public boolean addRole(String username, Role role) {
        User user = userRepository.findByUsername(username);
        if (user == null) return false;

        Set<Role> set = user.getRoles();
        boolean result = set.add(role);
        if (result) userRepository.save(user);
        return result;
    }

    @Transactional
    public boolean removeRole(String username, Role role) {
        User user = userRepository.findByUsername(username);
        if (user == null) return false;

        Set<Role> set = user.getRoles();
        boolean result = set.remove(role);
        if (result) userRepository.save(user);
        return result;
    }
}
