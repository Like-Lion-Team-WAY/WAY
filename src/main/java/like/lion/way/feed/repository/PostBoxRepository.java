package like.lion.way.feed.repository;

import like.lion.way.feed.domain.PostBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostBoxRepository extends JpaRepository<PostBox, Long> {
}
