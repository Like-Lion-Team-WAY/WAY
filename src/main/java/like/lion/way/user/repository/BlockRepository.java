package like.lion.way.user.repository;

import java.util.List;
import like.lion.way.user.domain.Block;
import like.lion.way.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<Block,Long> {
    List<Block> findAllByBlockerUserId(User user);
    void deleteByBlockerUserIdAndBlockedUserId(User blockUser , User blockedUser);
}
