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
    Page<BoardPost> findAllByBoardOrderByCreatedAtDesc(Board board, Pageable pageable);

    @Query("SELECT bp FROM BoardPost bp WHERE bp.id = :boardPostId")
    BoardPost findByBoardPostId(@Param("boardPostId") Long boardPostId);

    @Query("SELECT bp FROM BoardPost bp " +
            "WHERE (SELECT COUNT(BPL) FROM BoardPostLike BPL WHERE BPL.boardPost.id = bp.id) >= 10 " +
            "ORDER BY (SELECT COUNT(BPL) FROM BoardPostLike BPL WHERE BPL.boardPost.id = bp.id) DESC")
    List<BoardPost> findTop10BoardPostsByLikes(Pageable pageable);

    @Query("SELECT bp FROM BoardPost bp WHERE bp.board.id = :boardId AND (bp.title LIKE %:keywords% OR bp.content LIKE %:keywords%) ORDER BY bp.createdAt DESC")
    Page<BoardPost> findByBoardIdAndSearchKeywords(@Param("boardId") Long boardId, @Param("keywords") String keywords, Pageable pageable);


}
