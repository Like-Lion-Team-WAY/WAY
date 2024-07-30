package like.lion.way.user.repository;

import like.lion.way.user.domain.BlueCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlueCheckRepository extends JpaRepository<BlueCheck,Long> {
}
