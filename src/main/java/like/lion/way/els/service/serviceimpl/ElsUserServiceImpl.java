package like.lion.way.els.service.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import like.lion.way.els.domain.ElsUser;
import like.lion.way.els.repository.ElsUserRepository;
import like.lion.way.els.service.ElsUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElsUserServiceImpl implements ElsUserService {
    private final ElsUserRepository elsUserRepository;
    /**
     * 사용자 저장
     * @param elsUser
     */
    @Override
    public ElsUser saveOrUpdate(ElsUser elsUser) { //수정 등록
        return elsUserRepository.save(elsUser);
    }
    /**
     * 사용자 조회
     * @param userId
     */
    @Override
    public ElsUser findByUserId(Long userId) {
        return elsUserRepository.findById(String.valueOf(userId)).orElse(null);
    }
    /**
     * 모든 사용자 조회
     */
    @Override
    public List<ElsUser> getAllUsers() {
        // Iterable을 List로 변환
        Iterable<ElsUser> iterable = elsUserRepository.findAll();
        List<ElsUser> elsUsers = new ArrayList<>();
        iterable.forEach(elsUsers::add);
        return elsUsers;
    }
    /**
     * 사용자 삭제
     * @param userId
     */
    @Override
    public boolean deleteByUserId(String userId) {
        if (elsUserRepository.existsById(userId)) {
            elsUserRepository.deleteById(userId);
            return true;
        }
        return false;
    }
    /**
     * 사용자 검색 (관심사 + username + nickname)
     * @param interest
     */
    public List<ElsUser> searchUsersByInterest(String interest) {
        List<ElsUser> users = elsUserRepository.findByInterestsContaining(interest);
        return users;
    }

}
