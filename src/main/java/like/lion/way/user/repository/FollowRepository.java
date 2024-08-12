package like.lion.way.user.repository;

import java.util.List;
import like.lion.way.user.domain.Follow;
import like.lion.way.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {
    List<Follow> findAllByFollowing(User user);

    List<Follow> findAllByFollower(User user);

    Follow findByFollowerAndFollowing(User follower , User following);

    void deleteByFollowerAndFollowing(User deleteUser, User deletedUser);
}
