package like.lion.way.board.repository;

import like.lion.way.board.domain.BoardPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardPostRepository extends JpaRepository<BoardPost, Long> {
}
