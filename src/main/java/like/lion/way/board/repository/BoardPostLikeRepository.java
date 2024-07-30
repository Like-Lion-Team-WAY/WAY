package like.lion.way.board.repository;

import like.lion.way.board.domain.BoardPostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardPostLikeRepository extends JpaRepository<BoardPostLike, Long> {
}
