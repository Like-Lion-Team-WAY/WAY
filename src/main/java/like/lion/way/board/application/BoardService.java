package like.lion.way.board.application;

import jakarta.servlet.http.HttpServletRequest;
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
    void createBoard(BoardCreateServiceRequest request, HttpServletRequest httpServletRequest);
    void updateBoard(BoardEditServiceRequest request, String name);
    void deleteBoard(String name);
    Page<BoardPostResponse> getPostFindAll(String name, Pageable pageable);
    void createPost(String boardName, BoardPostCreateServiceRequest request, HttpServletRequest httpServletRequest);

}
