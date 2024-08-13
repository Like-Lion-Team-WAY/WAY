package like.lion.way.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;
import like.lion.way.user.domain.User;
import like.lion.way.user.dto.SettingLoginInfoDto;
import like.lion.way.user.dto.UserProfileDto;
import like.lion.way.user.oauth2.dto.OAuthAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    User findByUserId(Long userId);
    User findByUsername(String username);
    User saveOrUpdate(OAuthAttributes attributes);
    User findByEmail(String email);

    void addCookies(HttpServletResponse response, User user);
    void deleteUser(Long userId);
    void deleteCookie(HttpServletResponse response);

    User updateLoginInfo(SettingLoginInfoDto loginInfoDto, HttpServletRequest request, HttpServletResponse response);
    User saveOrUpdateUser(User user);

    User addInterests(HttpServletRequest request, HttpServletResponse response, Set<String> interests);

    UserProfileDto getProfile(HttpServletRequest request);

    User updateUserInfo(SettingLoginInfoDto updateUserDto, HttpServletRequest request);

    ResponseEntity<String> updateOrSaveImg(MultipartFile file ,String deleteFileName , HttpServletRequest request);
    User getUserByToken(HttpServletRequest request);

    void logout(HttpServletResponse response);
}
