package like.lion.way.board.repository;

import like.lion.way.board.domain.BoardPostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardPostCommentRepository extends JpaRepository<BoardPostComment, Long> {
}
