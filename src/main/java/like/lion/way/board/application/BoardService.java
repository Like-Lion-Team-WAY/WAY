package like.lion.way.board.application;

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
    public void createBoard(BoardCreateServiceRequest request, String token);
    public void updateBoard(BoardEditServiceRequest request, Long boardId);
    public void deleteBoard(Long boardId);
    public Page<BoardPostResponse> getPostFindAll(String name, Pageable pageable);
    public void createPost(String boardName, BoardPostCreateServiceRequest request, String token);

}
