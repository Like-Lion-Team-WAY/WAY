package like.lion.way.els.service;

import java.util.List;
import like.lion.way.els.domain.ElsInterest;

public interface ElsInterestService {
    List<String> searchInterestsByDescription(String keyword);

    List<ElsInterest> getAllInterests();

    ElsInterest saveInterest(ElsInterest interest);
}
