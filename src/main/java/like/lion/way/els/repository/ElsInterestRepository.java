package like.lion.way.els.repository;

import java.util.List;
import like.lion.way.els.domain.ElsInterest;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElsInterestRepository extends ElasticsearchRepository<ElsInterest,Long> {
    @Query("{\"match\": {\"description\": \"?0\"}}")
    List<ElsInterest> findByKeyword(String keyword);
}
