package like.lion.way.user.service;

import like.lion.way.user.domain.User;
import like.lion.way.user.oauth2.dto.OAuthAttributes;

public interface UserService {

    User findByUserId(Long userId);
    User findByUsername(String username);
    User saveOrUpdate(OAuthAttributes attributes);
    User findByEmail(String email);
}
