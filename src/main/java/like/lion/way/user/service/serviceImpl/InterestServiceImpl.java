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

    @Override
    public Interest findByInterestName(String interestName) {
        return interestRepository.findByInterestName(interestName).orElse(null);
    }

    @Transactional
    @Override
    public Interest saveInterest(Interest interest) {
        return interestRepository.save(interest);
    }
}