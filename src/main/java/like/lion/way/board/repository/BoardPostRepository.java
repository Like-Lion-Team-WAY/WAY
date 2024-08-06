package like.lion.way.board.repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import like.lion.way.board.domain.Board;
import like.lion.way.board.domain.BoardPost;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardPostRepository extends JpaRepository<BoardPost, Long> {

    List<BoardPost> findAllByBoard(Board board);
    Page<BoardPost> findAllByBoard(Board board, Pageable pageable);

    @Query("SELECT bp FROM BoardPost bp WHERE bp.id = :boardPostId")
    BoardPost findByBoardPostId(@Param("boardPostId") Long boardPostId);


}
