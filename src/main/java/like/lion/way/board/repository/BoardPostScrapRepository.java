package like.lion.way.board.repository;

import like.lion.way.board.domain.BoardPostScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardPostScrapRepository extends JpaRepository<BoardPostScrap, Long> {
}
