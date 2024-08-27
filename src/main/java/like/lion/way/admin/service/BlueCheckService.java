package like.lion.way.admin.service;

import java.util.List;
import like.lion.way.admin.domain.BlueCheck;
import like.lion.way.user.domain.User;

public interface BlueCheckService {
    BlueCheck applyBlueCheck(Long userId);

    BlueCheck findByUser(User user);

    List<BlueCheck> findAll();
}
