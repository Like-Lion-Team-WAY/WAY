package like.lion.way.feed.repository;

import java.util.List;
import like.lion.way.feed.domain.Question;
import like.lion.way.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Question getByQuestionId(Long questionId);

    List<Question> findQuestionsByAnswerer(User user);

    List<Question> findQuestionsByQuestioner(User user);
}
