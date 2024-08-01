package like.lion.way.user.repository;


import java.util.Optional;
import like.lion.way.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsername(String username);
    Optional<User> findByEmail(String email);
    User findByProviderId(String providerId);


}
