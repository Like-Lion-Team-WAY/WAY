package like.lion.way.user.service;

import like.lion.way.user.domain.User;

public interface UserService {

    User findByUserId(Long userId);
    User findByUsername(String username);

}
