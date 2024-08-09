package like.lion.way.board.repository;

import like.lion.way.board.domain.BoardPost;
import like.lion.way.board.domain.BoardPostScrap;
import like.lion.way.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardPostScrapRepository extends JpaRepository<BoardPostScrap, Long> {

    @Query("SELECT count(scrap) from BoardPostScrap scrap where scrap.boardPost.id = ?1")
    Long countScrapsByBoardPostId(Long postId);
    BoardPostScrap findBoardPostScrapByBoardPostAndUser(BoardPost boardPost, User user);

}
