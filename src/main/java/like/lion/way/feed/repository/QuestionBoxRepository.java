package like.lion.way.feed.repository;

import like.lion.way.feed.domain.QuestionBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionBoxRepository extends JpaRepository<QuestionBox, Long> {
}
