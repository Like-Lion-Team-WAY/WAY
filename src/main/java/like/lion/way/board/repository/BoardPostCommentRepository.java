package like.lion.way.board.repository;

import like.lion.way.board.domain.BoardPostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardPostCommentRepository extends JpaRepository<BoardPostComment, Long> {

    @Query("SELECT COUNT (comment) FROM BoardPostComment comment WHERE comment.boardPost.id = ?1")
    Long countCommentsByBoardPostIdt(Long postId);

}
