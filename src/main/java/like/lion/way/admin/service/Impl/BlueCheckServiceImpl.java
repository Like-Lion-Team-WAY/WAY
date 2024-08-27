package like.lion.way.admin.service.Impl;

import java.time.LocalDateTime;
import java.util.List;
import like.lion.way.admin.domain.BlueCheck;
import like.lion.way.admin.repository.BlueCheckRepository;
import like.lion.way.admin.service.BlueCheckService;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlueCheckServiceImpl implements BlueCheckService {
    private final UserService userService;
    private final BlueCheckRepository blueCheckRepository;

    @Override
    public BlueCheck applyBlueCheck(Long userId) {

        User user = userService.findByUserId(userId);

        if (user == null) {
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }

        BlueCheck blueCheck = new BlueCheck();
        blueCheck.setUser(user);
        blueCheck.setBlueCheckDate(LocalDateTime.now());
        return blueCheckRepository.save(blueCheck);
    }

    @Override
    public BlueCheck findByUser(User user) {
        return blueCheckRepository.findByUser(user);
    }

    @Override
    public List<BlueCheck> findAll() {
        return blueCheckRepository.findAll();
    }
}
