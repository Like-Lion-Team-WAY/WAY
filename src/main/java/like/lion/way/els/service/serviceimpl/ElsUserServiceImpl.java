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

    @Override
    public ElsUser saveOrUpdate(ElsUser elsUser) { //수정 등록
        return elsUserRepository.save(elsUser);
    }

    @Override
    public ElsUser findByUserId(Long userId) {
        return elsUserRepository.findById(String.valueOf(userId)).orElse(null);
    }

    @Override
    public List<ElsUser> getAllUsers() {
        // Iterable을 List로 변환
        Iterable<ElsUser> iterable = elsUserRepository.findAll();
        List<ElsUser> elsUsers = new ArrayList<>();
        iterable.forEach(elsUsers::add);
        return elsUsers;
    }

//    @Override
//    public List<ElsUser> searchUsersByUsername(String username) {
//        return elsUserRepository.findByUsernameContaining(username);
//    }

    @Override
    public boolean deleteByUserId(String userId) {
        if (elsUserRepository.existsById(userId)) {
            elsUserRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public List<ElsUser> searchUsersByInterest(String interest) {
        List<ElsUser> users = elsUserRepository.findByInterestsContaining(interest);
        return users;
    }

}
