package like.lion.way.feed.repository;

import like.lion.way.feed.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Question getByQuestionId(Long questionId);
}
