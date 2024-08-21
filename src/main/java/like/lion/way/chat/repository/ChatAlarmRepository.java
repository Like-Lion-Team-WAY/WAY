package like.lion.way.chat.repository;

import java.util.Optional;
import like.lion.way.chat.domain.ChatAlarm;
import like.lion.way.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatAlarmRepository extends JpaRepository<ChatAlarm, Long> {
    Optional<ChatAlarm> findByUser(User user);

}
