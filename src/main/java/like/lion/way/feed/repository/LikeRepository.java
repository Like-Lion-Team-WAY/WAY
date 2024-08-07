package like.lion.way.feed.repository;

import like.lion.way.feed.domain.Like;
import like.lion.way.feed.domain.Post;
import like.lion.way.feed.domain.Question;
import like.lion.way.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByUserAndPost(User user, Post post);

    Like findByUserAndQuestion(User user, Question question);
}
