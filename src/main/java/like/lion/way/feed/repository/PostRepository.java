package like.lion.way.feed.repository;

import java.util.List;
import like.lion.way.feed.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByPostCreatedAtAsc();
}
