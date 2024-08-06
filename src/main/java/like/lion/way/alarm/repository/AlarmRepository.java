package like.lion.way.alarm.repository;

import like.lion.way.alarm.domain.Alarm;
import like.lion.way.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Long countByUser(User user);
}
