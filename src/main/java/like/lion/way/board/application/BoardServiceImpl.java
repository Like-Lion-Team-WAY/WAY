package like.lion.way.board.application;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import like.lion.way.board.application.request.BoardCreateServiceRequest;
import like.lion.way.board.application.request.BoardEditServiceRequest;
import like.lion.way.board.application.request.BoardPostCreateServiceRequest;
import like.lion.way.board.application.response.BoardPostResponse;
import like.lion.way.board.application.response.BoardTitleResponse;
import like.lion.way.board.domain.Board;
import like.lion.way.board.domain.BoardPost;
import like.lion.way.board.repository.BoardPostRepository;
import like.lion.way.board.repository.BoardRepository;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardPostRepository boardPostRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Override
    public List<BoardTitleResponse> getBoardFindAll() {

        List<Board> boards = boardRepository.findAll();
        return boards.stream()
                .map(board -> BoardTitleResponse.builder()
                        .name(board.getName())
                        .build())
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public void createBoard(BoardCreateServiceRequest request, HttpServletRequest httpServletRequest) {

        User user = getUserByHttpServletRequest(httpServletRequest);
        log.info("사용자 정보 테스트 :::" + user);
        boardRepository.save(request.toEntity(user));

    }

    @Override
    @Transactional
    public void updateBoard(BoardEditServiceRequest request, String name) {

        Board board = boardRepository.findByName(name);
        board.updateBoard(request.getName(), request.getIntroduction(), request.isAnonymousPermission());

    }

    @Override
    @Transactional
    public void deleteBoard(String name) {

        Board board = boardRepository.findByName(name);
        boardRepository.delete(board);

    }

    @Override
    public Page<BoardPostResponse> getPostFindAll(String name, Pageable pageable) {
        Board board = boardRepository.findByName(name);
        Page<BoardPost> postsPage = boardPostRepository.findAllByBoard(board, pageable);

        List<BoardPostResponse> postResponses = postsPage.stream()
                .map(post -> BoardPostResponse.builder()
                        .boardName(name)
                        .postTitle(post.getTitle())
                        .author(post.getUser().getNickname(post.isAnonymousPermission()))
                        .created_at(post.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(postResponses, pageable, postsPage.getTotalElements());
    }

    @Override
    @Transactional
    public void createPost(String name, BoardPostCreateServiceRequest request, HttpServletRequest httpServletRequest) {

        Board board = boardRepository.findByName(name);
        log.info("게시판 정보 ::: " + board);
        User user = getUserByHttpServletRequest(httpServletRequest);
        boardPostRepository.save(request.toEntity(user, board));

    }


    private User getUserByHttpServletRequest(HttpServletRequest httpRequest) {

        try {
            String token = jwtUtil.getCookieValue(httpRequest, "accessToken");
            Long userId = jwtUtil.getUserIdFromToken(token);
            return userService.findByUserId(userId);
        } catch (Exception e) {
            log.error("Invalid token", e);
            return null;
        }

    }

}
