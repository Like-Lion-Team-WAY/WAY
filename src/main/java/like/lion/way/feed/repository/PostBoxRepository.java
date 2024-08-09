package like.lion.way.feed.repository;

import java.util.List;
import java.util.Optional;
import like.lion.way.feed.domain.Post;
import like.lion.way.feed.domain.PostBox;
import like.lion.way.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostBoxRepository extends JpaRepository<PostBox, Long> {
    Optional<PostBox> findByUserAndPost(User user, Post post);

    List<PostBox> findByPost(Post post);

    List<PostBox> findByUser(User user);
}
