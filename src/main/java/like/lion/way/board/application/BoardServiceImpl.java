package like.lion.way.board.application;

import java.util.List;
import java.util.stream.Collectors;
import like.lion.way.board.application.request.BoardCreateServiceRequest;
import like.lion.way.board.application.request.BoardEditServiceRequest;
import like.lion.way.board.application.response.BoardTitleResponse;
import like.lion.way.board.domain.Board;
import like.lion.way.board.repository.BoardRepository;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

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
    public void createBoard(BoardCreateServiceRequest request, String token) {

        User user = getUserByToken(token);
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

    private User getUserByToken(String token) {

        try {
            String username = jwtUtil.getUsernameFromAccessToken(token);
            return userService.findByUsername(username);
        } catch (Exception e) {
            log.error("Invalid token", e);
            return null;
        }

    }

}
