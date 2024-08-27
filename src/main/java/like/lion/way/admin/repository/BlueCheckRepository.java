package like.lion.way.admin.repository;

import like.lion.way.admin.domain.BlueCheck;
import like.lion.way.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlueCheckRepository extends JpaRepository<BlueCheck, Long> {
    BlueCheck findByUser(User user);
}
