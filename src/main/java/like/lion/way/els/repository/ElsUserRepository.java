package like.lion.way.els.repository;

import java.util.List;
import like.lion.way.els.domain.ElsUser;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElsUserRepository extends ElasticsearchRepository<ElsUser, String> {
//    @Query("{\"bool\": {\"must\": [{\"match\": {\"username\": {\"query\": \"?0\", \"operator\": \"and\"}}}]}}")
    List<ElsUser> findByUsernameContaining(String username);

    List<ElsUser> findByInterestsContaining(String interest);
}
