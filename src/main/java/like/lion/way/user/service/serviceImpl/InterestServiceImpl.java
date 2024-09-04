package like.lion.way.user.service.serviceImpl;

import like.lion.way.user.domain.Interest;
import like.lion.way.user.repository.InterestRepository;
import like.lion.way.user.service.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService {

    private final InterestRepository interestRepository;

    /**
     * 기존에 있는 관심태그인지 아닌지 체크하고 없으면 save
     * @param interestName 관심태그이름
     */
    @Transactional
    @Override
    public Interest findOrSaveInterest(String interestName) {
        Interest interest = findByInterestName(interestName);
        if(interest==null){
            Interest save = new Interest();
            save.setInterestName(interestName);
            return saveInterest(save);
        }else{
           return interest;
        }
    }

    /**
     * interestName으로 interest찾기
     * @param interestName 관심태그이름
     */
    @Override
    public Interest findByInterestName(String interestName) {
        return interestRepository.findByInterestName(interestName).orElse(null);
    }

    /**
     * 관심사 저장
     * @param interest 관심사객체
     */
    @Transactional
    @Override
    public Interest saveInterest(Interest interest) {
        return interestRepository.save(interest);
    }
}