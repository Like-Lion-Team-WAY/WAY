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

    /**
     * 인증 배지 신청
     * @param userId 인증 배지 신청할 유저의 userId
     * @return 생성한 인증 배지 신청서
     */
    @Override
    public BlueCheck applyBlueCheck(Long userId) {

        User user = userService.findByUserId(userId);

        if (user == null) {
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        } else if(blueCheckRepository.findByUser(user) != null) {
            throw new IllegalArgumentException("이미 블루체크를 신청한 유저입니다.");
        }

        BlueCheck blueCheck = new BlueCheck();
        blueCheck.setUser(user);
        blueCheck.setBlueCheckDate(LocalDateTime.now());
        return blueCheckRepository.save(blueCheck);
    }

    /**
     * 해당 사용자의 인증 배지 신청 여부 조회
     * @param user 조회할 사용자
     * @return 조회한 인증 배지 신청서
     */
    @Override
    public BlueCheck findByUser(User user) {
        return blueCheckRepository.findByUser(user);
    }

    /**
     * 모든 사용자의 인증 배지 신청 목록 조회
     * @return 인증 배지 신청서 목록
     */
    @Override
    public List<BlueCheck> findAll() {
        return blueCheckRepository.findAll();
    }

    /**
     * 인증 배지 신청 내역 삭제
     * @param username 삭제할 사용자의 username
     * @throws IllegalArgumentException 사용자가 존재하지 않거나 인증배지를 신청하지 않은 경우
     * @throws NullPointerException 인증배지를 신청하지 않은 사용자의 경우
     */
    @Override
    public void removeBlueCheck(String username) throws IllegalArgumentException, NullPointerException {
        User user = userService.findByUsername(username);
        BlueCheck blueCheck = blueCheckRepository.findByUser(user);
        blueCheckRepository.delete(blueCheck);
    }
}
