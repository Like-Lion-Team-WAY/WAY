package like.lion.way.board.application;

import jakarta.servlet.http.HttpServletRequest;
import like.lion.way.board.application.request.BoardPostCommentServiceRequest;
import like.lion.way.board.application.response.BoardPostCommentCountResponse;
import like.lion.way.board.application.response.BoardPostDetailResponse;
import like.lion.way.board.application.response.BoardPostLikeCountResponse;
import like.lion.way.board.application.response.BoardPostScrapCountResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import like.lion.way.board.application.request.BoardCreateServiceRequest;
import like.lion.way.board.application.request.BoardEditServiceRequest;
import like.lion.way.board.application.request.BoardPostCreateServiceRequest;
import like.lion.way.board.application.response.BoardPostResponse;
import like.lion.way.board.application.response.BoardTitleResponse;


public interface BoardService {

    List<BoardTitleResponse> getBoardFindAll();

    BoardTitleResponse getBoardTitle(Long boardId);

    void createBoard(BoardCreateServiceRequest request, HttpServletRequest httpServletRequest);

    void updateBoard(BoardEditServiceRequest request, Long boardId);

    void deleteBoard(Long boardId);

    Page<BoardPostResponse> getPostFindAll(Long boardId, Pageable pageable);

    void createPost(Long boardId, BoardPostCreateServiceRequest request, HttpServletRequest httpServletRequest);

    //    BoardPostDetailResponse getPostDetails(String boardName, String postTitle);
    BoardPostLikeCountResponse getPostLikeCount(Long postId);

    void likePost(Long postId, HttpServletRequest httpServletRequest);

    BoardPostScrapCountResponse getPostScrapCount(Long postId);

    void scrapPost(Long postId, HttpServletRequest httpServletRequest);

    BoardPostCommentCountResponse getPostCommentCount(Long postId);

    void commentPost(Long postId, BoardPostCommentServiceRequest request, HttpServletRequest httpServletRequest);

}
