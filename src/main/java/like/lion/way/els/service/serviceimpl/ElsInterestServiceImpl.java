package like.lion.way.els.service.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import like.lion.way.els.domain.ElsInterest;
import like.lion.way.els.repository.ElsInterestRepository;
import like.lion.way.els.service.ElsInterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElsInterestServiceImpl implements ElsInterestService {

    private final ElsInterestRepository elsInterestRepository;

    /**
     * 설명으로 관심사 찾기
     */
    @Override
    public List<String> searchInterestsByDescription(String keyword) {
        List<ElsInterest> interests = elsInterestRepository.findByKeyword(keyword);

        return interests.stream().map(ElsInterest::getName).toList();
    }

    /**
     * 모든 관심사 가져오기
     */
    @Override
    public List<ElsInterest> getAllInterests() {
        // Iterable을 List로 변환
        return StreamSupport.stream(elsInterestRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * 관심사 생성
     */
    @Override
    public ElsInterest saveInterest(ElsInterest interest) {
        return elsInterestRepository.save(interest);
    }
}
