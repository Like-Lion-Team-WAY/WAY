package like.lion.way.alarm.repository;

import java.util.Optional;
import like.lion.way.alarm.domain.AlarmSetting;
import like.lion.way.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmSettingRepository extends JpaRepository<AlarmSetting, Long> {
    Optional<AlarmSetting> findByUser(User user);

    Optional<AlarmSetting> findByUser_UserId(Long userId);
}
