package like.lion.way.alarm.repository;

import like.lion.way.alarm.domain.Alarm;
import like.lion.way.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Long countByUser(User user);
    Long countByUser_UserId(Long userId);
    Page<Alarm> findByUser_UserId(Long userID, Pageable pageable);
    void deleteByUser_UserId(Long userId);
}
