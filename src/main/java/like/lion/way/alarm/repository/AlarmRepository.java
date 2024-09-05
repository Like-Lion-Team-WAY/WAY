package like.lion.way.alarm.repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import like.lion.way.alarm.domain.Alarm;
import like.lion.way.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Long countByUser(User user);
    Long countByUser_UserId(Long userId);
    Page<Alarm> findByUser_UserId(Long userID, Pageable pageable);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Alarm> findByUser_UserId(Long userId);
    void deleteByUser_UserId(Long userId);
}
