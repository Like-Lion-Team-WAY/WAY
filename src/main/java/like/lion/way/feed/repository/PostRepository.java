package like.lion.way.feed.repository;

import java.util.List;
import like.lion.way.feed.domain.Post;
import like.lion.way.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByPostCreatedAtAsc();

    List<Post> findPostByUser(User user);
}
