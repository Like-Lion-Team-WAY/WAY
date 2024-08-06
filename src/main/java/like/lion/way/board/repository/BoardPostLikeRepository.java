package like.lion.way.board.repository;

import like.lion.way.board.domain.BoardPost;
import like.lion.way.board.domain.BoardPostLike;
import like.lion.way.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardPostLikeRepository extends JpaRepository<BoardPostLike, Long> {

    @Query("SELECT COUNT (like) FROM BoardPostLike like WHERE like.boardPost.title = ?1")
    Long countLikesByTitle(String title);

    @Query("SELECT postLike FROM BoardPostLike postLike WHERE postLike.boardPost = ?1 AND postLike.user = ?2")
    BoardPostLike findBoardPostLikeByBoardPostAndAndUser(BoardPost boardPost, User user);

}
