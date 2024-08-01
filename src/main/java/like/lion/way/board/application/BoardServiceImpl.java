package like.lion.way.board.application;

import java.util.List;
import java.util.stream.Collectors;
import like.lion.way.board.application.request.BoardCreateServiceRequest;
import like.lion.way.board.application.request.BoardEditServiceRequest;
import like.lion.way.board.application.response.BoardTitleResponse;
import like.lion.way.board.domain.Board;
import like.lion.way.board.repository.BoardRepository;
import like.lion.way.user.domain.User;
import like.lion.way.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Override
    public List<BoardTitleResponse> getBoardFindAll() {

        List<Board> boards = boardRepository.findAll();

        return boards.stream()
                .map(board -> BoardTitleResponse.builder()
                        .boardId(board.getId())
                        .name(board.getName())
                        .build())
                .collect(Collectors.toList());

    }

    @Override
    public void createBoard(BoardCreateServiceRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        boardRepository.save(request.toEntity(user));

    }

    @Override
    public void updateBoard(BoardEditServiceRequest request, Long boardId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        board.updateBoard(request.getName(), request.getIntroduction(), request.isAnonymousPermission());

    }

    @Override
    public void deleteBoard(Long boardId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        boardRepository.delete(board);

    }


}
