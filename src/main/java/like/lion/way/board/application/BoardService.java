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
import like.lion.way.board.domain.Board;
import like.lion.way.board.domain.BoardPost;

public interface BoardService {

    public List<BoardTitleResponse> getBoardFindAll();
    public void createBoard(BoardCreateServiceRequest request, HttpServletRequest httpServletRequest);
    public void updateBoard(BoardEditServiceRequest request, String name);
    public void deleteBoard(String name);
    public Page<BoardPostResponse> getPostFindAll(String name, Pageable pageable);
    public void createPost(String boardName, BoardPostCreateServiceRequest request, HttpServletRequest httpServletRequest);

}
