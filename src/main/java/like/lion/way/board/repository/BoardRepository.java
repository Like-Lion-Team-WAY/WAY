package like.lion.way.board.repository;

import java.util.List;
import like.lion.way.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Board findByName(String name);

    @Query("SELECT board FROM Board board WHERE board.name LIKE %?1% OR board.introduction LIKE %?1%")
    List<Board> findBySearchKeywords(String keywords);
//    @Query("SELECT board FROM Board board WHERE ?1 IN (board.name, board.introduction)")
//    List<Board> findBySearchKeywords(String keywords);

}
