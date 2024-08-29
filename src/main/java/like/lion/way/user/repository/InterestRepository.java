package like.lion.way.user.repository;

import java.util.Optional;
import like.lion.way.user.domain.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestRepository extends JpaRepository<Interest,Long> {

    Optional<Interest> findByInterestName(String InterestName);
}