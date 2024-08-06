package like.lion.way.chat.repository;

import java.util.List;
import like.lion.way.chat.domain.Chat;
import like.lion.way.feed.domain.Question;
import like.lion.way.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByUser1AndUserActive1TrueOrUser2AndUserActive2True(User user1, User user2);
    Chat findFirstByQuestionOrderByCreatedAtDesc(Question question);
}
