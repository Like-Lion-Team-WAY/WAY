package like.lion.way.chat.repository;

import like.lion.way.chat.domain.Chat;
import like.lion.way.feed.domain.Question;
import like.lion.way.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Page<Chat> findByUser1AndUserActive1TrueOrUser2AndUserActive2True(User user1, User user2, Pageable pageable);
    Chat findFirstByQuestionOrderByCreatedAtDesc(Question question);
}
