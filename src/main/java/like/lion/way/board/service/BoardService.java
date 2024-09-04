package like.lion.way.board.service;

import jakarta.servlet.http.HttpServletRequest;
import like.lion.way.board.dto.request.BoardPostCommentServiceRequest;
import like.lion.way.board.dto.request.BoardPostEditServiceRequest;
import like.lion.way.board.dto.request.BoardSearchServiceRequest;
import like.lion.way.board.dto.response.BoardBestPostResponse;
import like.lion.way.board.dto.response.BoardPostCommentCountResponse;
import like.lion.way.board.dto.response.BoardPostDetailResponse;
import like.lion.way.board.dto.response.BoardPostLikeCountResponse;
import like.lion.way.board.dto.response.BoardPostScrapCountResponse;

import like.lion.way.board.dto.response.BoardPostScrapsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import like.lion.way.board.dto.request.BoardCreateServiceRequest;
import like.lion.way.board.dto.request.BoardEditServiceRequest;
import like.lion.way.board.dto.request.BoardPostCreateServiceRequest;
import like.lion.way.board.dto.response.BoardPostResponse;
import like.lion.way.board.dto.response.BoardTitleResponse;


public interface BoardService {

    Page<BoardTitleResponse> getBoardFindAll(Pageable pageable);

    BoardTitleResponse getBoardTitle(Long boardId, HttpServletRequest httpServletRequest);

    void createBoard(BoardCreateServiceRequest request, HttpServletRequest httpServletRequest);

    void updateBoard(BoardEditServiceRequest request, Long boardId);

    void deleteBoard(Long boardId);

    Page<BoardPostResponse> getPostFindAll(Long boardId, Pageable pageable);

    void createPost(Long boardId, BoardPostCreateServiceRequest request, HttpServletRequest httpServletRequest);

    BoardPostDetailResponse getPostDetails(Long postId, HttpServletRequest httpServletRequest);

    void editBoardPost(Long boardId, BoardPostEditServiceRequest request);

    void deleteBoardPost(Long postId);

    BoardPostLikeCountResponse getPostLikeCount(Long postId);

    void likePost(Long postId, HttpServletRequest httpServletRequest);

    BoardPostScrapCountResponse getPostScrapCount(Long postId);

    void scrapPost(Long postId, HttpServletRequest httpServletRequest);

    BoardPostCommentCountResponse getPostCommentCount(Long postId);

    void commentPost(Long postId, BoardPostCommentServiceRequest request, HttpServletRequest httpServletRequest);

    Page<BoardPostScrapsResponse> getPostScraps(HttpServletRequest httpServletRequest, Pageable pageable);

    List<BoardBestPostResponse> getBestBoardPosts();

    List<BoardTitleResponse> getSearchBoards(BoardSearchServiceRequest request);

    Page<BoardPostResponse> getSearchBoardPosts(BoardSearchServiceRequest request, Pageable pageable);

}
