package like.lion.way.board.application;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import like.lion.way.board.application.request.BoardCreateServiceRequest;
import like.lion.way.board.application.request.BoardEditServiceRequest;
import like.lion.way.board.application.request.BoardPostCommentServiceRequest;
import like.lion.way.board.application.request.BoardPostCreateServiceRequest;
import like.lion.way.board.application.request.BoardPostEditServiceRequest;
import like.lion.way.board.application.request.BoardSearchServiceRequest;
import like.lion.way.board.application.response.BoardBestPostResponse;
import like.lion.way.board.application.response.BoardPostCommentCountResponse;
import like.lion.way.board.application.response.BoardPostCommentResponse;
import like.lion.way.board.application.response.BoardPostDetailResponse;
import like.lion.way.board.application.response.BoardPostLikeCountResponse;
import like.lion.way.board.application.response.BoardPostResponse;
import like.lion.way.board.application.response.BoardPostScrapCountResponse;
import like.lion.way.board.application.response.BoardPostScrapsResponse;
import like.lion.way.board.application.response.BoardTitleResponse;
import like.lion.way.board.domain.Board;
import like.lion.way.board.domain.BoardPost;
import like.lion.way.board.domain.BoardPostLike;
import like.lion.way.board.domain.BoardPostScrap;
import like.lion.way.board.repository.BoardPostCommentRepository;
import like.lion.way.board.repository.BoardPostLikeRepository;
import like.lion.way.board.repository.BoardPostRepository;
import like.lion.way.board.repository.BoardPostScrapRepository;
import like.lion.way.board.repository.BoardRepository;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    private final BoardPostLikeRepository boardPostLikeRepository;
    private final BoardPostScrapRepository boardPostScrapRepository;
    private final BoardPostCommentRepository boardPostCommentRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * 게시판 목록을 조회.
     *
     * @param pageable 페이지네이션 정보
     * @return 페이지네이션된 게시판 목록을 반환.
     */
    @Override
    public Page<BoardTitleResponse> getBoardFindAll(Pageable pageable) {

        Page<Board> boardsPage = boardRepository.findAll(pageable);
        List<BoardTitleResponse> boardTitles = boardsPage.stream()
                .map(board -> BoardTitleResponse.builder()
                        .boardId(board.getId())
                        .name(board.getName())
                        .introduction(board.getIntroduction())
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(boardTitles, pageable, boardsPage.getTotalElements());

    }

    /**
     * 게시판 제목과 소유자 일치 여부를 조회.
     *
     * @param boardId 게시판 ID
     * @param httpServletRequest 요청의 HttpServletRequest 객체
     * @return 게시판 제목과 소유자 일치 여부 정보를 반환.
     */
    @Override
    public BoardTitleResponse getBoardTitle(Long boardId, HttpServletRequest httpServletRequest) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        boolean userOwnerMatch = userOwnerMatch(httpServletRequest, board.getUser());
        return BoardTitleResponse.builder()
                .boardId(board.getId())
                .name(board.getName())
                .introduction(board.getIntroduction())
                .userOwnerMatch(userOwnerMatch)
                .build();

    }

    /**
     * 게시판을 생성.
     *
     * @param request 게시판 생성 요청 DTO
     * @param httpServletRequest 요청의 HttpServletRequest 객체
     */
    @Override
    @Transactional
    public void createBoard(BoardCreateServiceRequest request, HttpServletRequest httpServletRequest) {

        User user = getUserByHttpServletRequest(httpServletRequest);
        boardRepository.save(request.toEntity(user));

    }

    /**
     * 게시판을 수정.
     *
     * @param request 게시판 수정 요청 DTO
     * @param boardId 수정할 게시판의 ID
     */
    @Override
    @Transactional
    public void updateBoard(BoardEditServiceRequest request, Long boardId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));
        board.updateBoard(request.getName(), request.getIntroduction());

    }

    /**
     * 게시판을 삭제.
     *
     * @param boardId 삭제할 게시판의 ID
     */
    @Override
    @Transactional
    public void deleteBoard(Long boardId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        boardRepository.delete(board);

    }

    /**
     * 게시판의 게시글 목록을 조회.
     *
     * @param boardId 게시판 ID
     * @param pageable 페이지네이션 정보
     * @return 페이지네이션된 게시글 목록을 반환.
     */
    @Override
    public Page<BoardPostResponse> getPostFindAll(Long boardId, Pageable pageable) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        Page<BoardPost> postsPage = boardPostRepository.findAllByBoardOrderByCreatedAtDesc(board, pageable);

        List<BoardPostResponse> postResponses = postsPage.stream()
                .map(post -> BoardPostResponse.builder()
                        .boardName(board.getName())
                        .boardPostId(post.getId())
                        .postTitle(post.getTitle())
                        .nickname(nullUserNickCheck(post.getUser(), post.isAnonymousPermission()))
                        .created_at(post.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(postResponses, pageable, postsPage.getTotalElements());
    }

    /**
     * 게시글을 생성.
     *
     * @param boardId 게시판 ID
     * @param request 게시글 생성 요청 DTO
     * @param httpServletRequest 요청의 HttpServletRequest 객체
     */
    @Override
    @Transactional
    public void createPost(Long boardId, BoardPostCreateServiceRequest request, HttpServletRequest httpServletRequest) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));
        User user = getUserByHttpServletRequest(httpServletRequest);
        boardPostRepository.save(request.toEntity(user, board));

    }

    /**
     * 게시글의 상세 정보를 조회.
     *
     * @param postId 게시글 ID
     * @param httpServletRequest 요청의 HttpServletRequest 객체
     * @return 게시글의 상세 정보를 반환.
     */
    @Override
    public BoardPostDetailResponse getPostDetails(Long postId, HttpServletRequest httpServletRequest) {
        BoardPost post = boardPostRepository.findByBoardPostId(postId);
        boolean userOwnerMatch = userOwnerMatch(httpServletRequest, post.getUser());
        List<BoardPostCommentResponse> comments = boardPostCommentRepository.findByBoardPostId(postId).stream()
                .map(comment -> BoardPostCommentResponse.builder()
                        .commentId(comment.getId())
                        .commentUsername(nullUserCheck(comment.getUser()))
                        .commentContent(comment.getContent())
                        .commentCreatedAt(comment.getCreatedAt())
                        .preCommentId(comment.getPreCommentId())
                        .commentNickname(nullUserNickCheck(comment.getUser(), comment.isAnonymousPermission()))
                        .build())
                .collect(Collectors.toList());

        return BoardPostDetailResponse.builder()
                .nickname(nullUserNickCheck(post.getUser(), post.isAnonymousPermission()))
                .username(nullUserCheck(post.getUser()))
                .authorProfileImgUrl(nullUserImgCheck(post.getUser()))
                .postCreatedAt(post.getCreatedAt())
                .postTitle(post.getTitle())
                .postContent(post.getContent())
                .postLikes(boardPostLikeRepository.countLikesByBoardPostId(postId))
                .postComments(boardPostCommentRepository.countCommentsByBoardPostId(postId))
                .postScraps(boardPostScrapRepository.countScrapsByBoardPostId(postId))
                .boardPostCommentsList(comments)
                .userOwnerMatch(userOwnerMatch)
                .build();

    }

    /**
     * 게시글을 수정.
     *
     * @param postId 수정할 게시글의 ID
     * @param request 게시글 수정 요청 DTO
     */
    @Override
    @Transactional
    public void editBoardPost(Long postId, BoardPostEditServiceRequest request) {

        BoardPost boardPost = boardPostRepository.findByBoardPostId(postId);

        boardPost.editBoardPost(request.getTitle(), request.getContent());
    }

    /**
     * 게시글을 삭제.
     *
     * @param postId 삭제할 게시글의 ID
     */
    @Override
    @Transactional
    public void deleteBoardPost(Long postId) {
        BoardPost post = boardPostRepository.findByBoardPostId(postId);
        boardPostRepository.delete(post);
    }

    /**
     * 게시글의 좋아요 수를 반환.
     *
     * @param postId 게시글 ID
     * @return 게시글의 좋아요 수를 반환.
     */
    @Override
    public BoardPostLikeCountResponse getPostLikeCount(Long postId) {
        Long likes = boardPostLikeRepository.countLikesByBoardPostId(postId);
        return BoardPostLikeCountResponse.builder()
                .likes(likes)
                .build();
    }

    /**
     * 게시글에 좋아요를 추가하거나 제거.
     *
     * @param postId 게시글 ID
     * @param request 요청의 HttpServletRequest 객체
     */
    @Override
    @Transactional
    public void likePost(Long postId, HttpServletRequest request) {

        BoardPost post = boardPostRepository.findByBoardPostId(postId);
        User user = getUserByHttpServletRequest(request);

        BoardPostLike like = boardPostLikeRepository.findBoardPostLikeByBoardPostAndAndUser(post, user);

        if (like != null) {
            boardPostLikeRepository.delete(like);
        } else {
            boardPostLikeRepository.save(BoardPostLike.builder()
                    .post(post)
                    .user(user)
                    .build());
        }

    }

    /**
     * 게시글의 스크랩 수를 반환.
     *
     * @param postId 게시글 ID
     * @return 게시글의 스크랩 수를 반환.
     */
    @Override
    public BoardPostScrapCountResponse getPostScrapCount(Long postId) {
        Long scraps = boardPostScrapRepository.countScrapsByBoardPostId(postId);
        return BoardPostScrapCountResponse.builder()
                .scraps(scraps)
                .build();
    }

    /**
     * 게시글을 스크랩하거나 스크랩을 제거.
     *
     * @param postId 게시글 ID
     * @param httpServletRequest 요청의 HttpServletRequest 객체
     */
    @Override
    @Transactional
    public void scrapPost(Long postId, HttpServletRequest httpServletRequest) {

        BoardPost post = boardPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = getUserByHttpServletRequest(httpServletRequest);

        BoardPostScrap scrap = boardPostScrapRepository.findBoardPostScrapByBoardPostAndUser(post, user);

        if (scrap != null) {
            boardPostScrapRepository.delete(scrap);
        } else {
            boardPostScrapRepository.save(BoardPostScrap.builder()
                    .boardPost(post)
                    .user(user)
                    .build());
        }

    }

    /**
     * 게시글의 댓글 수를 반환.
     *
     * @param postId 게시글 ID
     * @return 게시글의 댓글 수를 반환.
     */
    @Override
    public BoardPostCommentCountResponse getPostCommentCount(Long postId) {

        Long comments = boardPostCommentRepository.countCommentsByBoardPostId(postId);
        return BoardPostCommentCountResponse.builder()
                .comments(comments)
                .build();

    }

    /**
     * 게시글에 댓글을 추가.
     *
     * @param postId 게시글 ID
     * @param request 댓글 작성 요청 DTO
     * @param httpServletRequest 요청의 HttpServletRequest 객체
     */
    @Override
    @Transactional
    public void commentPost(Long postId, BoardPostCommentServiceRequest request, HttpServletRequest httpServletRequest) {

        BoardPost post = boardPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = getUserByHttpServletRequest(httpServletRequest);

        boardPostCommentRepository.save(request.toEntity(post, user));

    }

    /**
     * 스크랩한 게시글 목록을 조회.
     *
     * @param httpServletRequest 요청의 HttpServletRequest 객체
     * @param pageable 페이지네이션 정보
     * @return 페이지네이션된 스크랩한 게시글 목록을 반환.
     */
    @Override
    public Page<BoardPostScrapsResponse> getPostScraps(HttpServletRequest httpServletRequest, Pageable pageable) {

        Page<BoardPostScrap> scrapsPage = boardPostScrapRepository.findAllBoardPostScrapByUser(getUserByHttpServletRequest(httpServletRequest), pageable);
        List<BoardPostScrapsResponse> scrapsResponses = scrapsPage.stream()
                .map(scrap -> BoardPostScrapsResponse.builder()
                        .title(scrap.getBoardPost().getTitle())
                        .nickname(nullUserNickCheck(scrap.getBoardPost().getUser(), scrap.getBoardPost().isAnonymousPermission()))
                        .username(nullUserCheck(scrap.getBoardPost().getUser()))
                        .createdAt(scrap.getBoardPost().getCreatedAt())
                        .boardId(scrap.getBoardPost().getBoard().getId())
                        .boardPostId(scrap.getBoardPost().getId())
                        .build())
                .toList();

        return new PageImpl<>(scrapsResponses, pageable, scrapsPage.getTotalElements());

    }

    /**
     * 베스트 게시판 게시글 목록을 조회.
     *
     * @return 베스트 게시판 게시글 목록을 반환.
     */
    @Override
    public List<BoardBestPostResponse> getBestBoardPosts() {
        Pageable pageable = PageRequest.of(0, 10); // 첫 페이지의 10개 항목
        List<BoardPost> top10Posts = boardPostRepository.findTop10BoardPostsByLikes(pageable);

        return top10Posts.stream()
                .map(boardPost -> BoardBestPostResponse.builder()
                        .boardTitle(boardPost.getTitle())
                        .boardId(boardPost.getBoard().getId())
                        .postId(boardPost.getId())
                        .likes(boardPostLikeRepository.countLikesByBoardPostId(boardPost.getId()))
                        .build())
                .toList();

    }

    /**
     * 게시판을 검색.
     *
     * @param request 검색 요청 DTO
     * @return 검색된 게시판 목록을 반환.
     */
    @Override
    public List<BoardTitleResponse> getSearchBoards(BoardSearchServiceRequest request) {

        List<Board> boards = boardRepository.findBySearchKeywords(request.getKeyword());

        return boards.stream()
                .map(board -> BoardTitleResponse.builder()
                        .boardId(board.getId())
                        .name(board.getName())
                        .introduction(board.getIntroduction())
                        .build())
                .toList();

    }

    /**
     * 게시글을 검색.
     *
     * @param request 검색 요청 DTO
     * @param pageable 페이지네이션 정보
     * @return 페이지네이션된 게시글 목록을 반환.
     */
    @Override
    public Page<BoardPostResponse> getSearchBoardPosts(BoardSearchServiceRequest request, Pageable pageable) {


        Page<BoardPost> postsPage = boardPostRepository.findByBoardIdAndSearchKeywords(request.getBoardId(), request.getKeyword(), pageable);
        List<BoardPostResponse> postResponses = postsPage.stream()
                .map(post -> BoardPostResponse.builder()
                        .boardPostId(post.getId())
                        .postTitle(post.getTitle())
                        .nickname(nullUserNickCheck(post.getUser(), post.isAnonymousPermission()))
                        .created_at(post.getCreatedAt())
                        .build())
                .toList();
        return new PageImpl<>(postResponses, pageable, postsPage.getTotalElements());

    }

    /**
     * HttpServletRequest에서 사용자 정보를 가져옴.
     *
     * @param httpRequest HttpServletRequest 객체
     * @return 사용자 정보를 반환.
     */
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

    /**
     * 로그인한 사용자와 게시물 소유자 일치 여부를 확인.
     *
     * @param httpServletRequest 요청의 HttpServletRequest 객체
     * @param owner 게시물 소유자
     * @return 일치 여부를 반환.
     */
    private boolean userOwnerMatch(HttpServletRequest httpServletRequest, User owner) {
        User user = getUserByHttpServletRequest(httpServletRequest);
        return user != null && user.equals(owner);
    }

    /**
     * 사용자 정보가 없을 경우 메시지를 반환.
     *
     * @param user 사용자 객체
     * @return 사용자 이름 또는 탈퇴 회원 메시지를 반환.
     */
    private String nullUserCheck(User user) {

        return user == null ? "탈퇴한 회원입니다." : user.getUsername();

    }

    /**
     * 사용자 닉네임을 반환하거나, 정보가 없을 경우 메시지를 반환.
     *
     * @param user 사용자 객체
     * @param permission 익명 여부 확인
     * @return 닉네임 또는 탈퇴 회원 메시지를 반환.
     */
    private String nullUserNickCheck(User user, boolean permission) {

        return user == null ? "탈퇴한 회원입니다." : user.getNickname(permission);

    }

    /**
     * 사용자 프로필 이미지를 반환하거나, 정보가 없을 경우 "null"을 반환.
     *
     * @param user 사용자 객체
     * @return 사용자 프로필 이미지 URL을 반환.
     */
    private String nullUserImgCheck(User user) {

        return user == null ? "null" : user.getUserImage();

    }

}
