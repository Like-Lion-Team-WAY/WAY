package like.lion.way.board.application;

import java.util.List;
import like.lion.way.board.application.request.BoardCreateServiceRequest;
import like.lion.way.board.application.request.BoardEditServiceRequest;
import like.lion.way.board.application.response.BoardTitleResponse;
import like.lion.way.board.domain.Board;

public interface BoardService {

    public List<BoardTitleResponse> getBoardFindAll();
    public void createBoard(BoardCreateServiceRequest request);
    public void updateBoard(BoardEditServiceRequest request, Long boardId);
    public void deleteBoard(Long boardId);

}
