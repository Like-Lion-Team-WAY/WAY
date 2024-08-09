package like.lion.way.feed.repository;

import java.util.List;
import like.lion.way.feed.domain.Question;
import like.lion.way.feed.domain.QuestionBox;
import like.lion.way.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionBoxRepository extends JpaRepository<QuestionBox, Long> {
    QuestionBox findByUserAndQuestion(User user, Question question);

    List<QuestionBox> findByUser(User user);
}
