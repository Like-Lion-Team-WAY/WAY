package like.lion.way.board.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import like.lion.way.ApiResponse;
import like.lion.way.board.api.request.BoardCreateRequest;
import like.lion.way.board.api.request.BoardEditRequest;
import like.lion.way.board.api.request.BoardPostCommentRequest;
import like.lion.way.board.api.request.BoardPostEditRequest;
import like.lion.way.board.api.request.BoardSearchRequest;
import like.lion.way.board.application.BoardService;
import like.lion.way.board.api.request.BoardPostCreateRequest;
import like.lion.way.board.application.response.BoardBestPostResponse;
import like.lion.way.board.application.response.BoardPostCommentCountResponse;
import like.lion.way.board.application.response.BoardPostDetailResponse;
import like.lion.way.board.application.response.BoardPostLikeCountResponse;
import like.lion.way.board.application.response.BoardPostResponse;
import like.lion.way.board.application.response.BoardPostScrapCountResponse;
import like.lion.way.board.application.response.BoardPostScrapsResponse;
import like.lion.way.board.application.response.BoardTitleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
@Slf4j
public class BoardRestController {

    private final BoardService boardService;

    /**
     * 게시판 목록을 요청.
     *
     * @param page 요청할 페이지 번호
     * @param size 페이지당 항목 수
     * @return 페이지네이션된 게시판 목록을 반환.
     */
    @GetMapping
    public ApiResponse<Page<BoardTitleResponse>> getBoardList(
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        Pageable pageable = PageRequest.of(page - 1, size);

        return ApiResponse.ok(boardService.getBoardFindAll(pageable));

    }

    /**
     * 게시판 정보를 요청 (제목, ID) + 작성자 일치 여부를 반환.
     *
     * @param boardId 요청할 게시판의 ID
     * @param httpServletRequest 요청의 HttpServletRequest 객체
     * @return 게시판의 제목과 ID, 작성자 일치 여부 정보를 반환.
     */
    @GetMapping("/{boardId}")
    public ApiResponse<BoardTitleResponse> getBoardTitle(@PathVariable("boardId") Long boardId,
                                                         HttpServletRequest httpServletRequest) {

        return ApiResponse.ok(boardService.getBoardTitle(boardId, httpServletRequest));

    }

    /**
     * 게시판을 생성.
     *
     * @param request 게시판 생성 요청 DTO
     * @param httpServletRequest 요청의 HttpServletRequest 객체
     * @return 성공 여부를 반환.
     */
    @PostMapping("/create")
    public ApiResponse<Void> createBoard(@RequestBody @Valid BoardCreateRequest request,
                                         HttpServletRequest httpServletRequest) {

        boardService.createBoard(request.toServiceRequest(), httpServletRequest);
        return ApiResponse.ok();

    }

    /**
     * 게시판을 수정.
     *
     * @param request 게시판 수정 요청 DTO
     * @param boardId 수정할 게시판의 ID
     * @return 성공 여부를 반환.
     */
    @PatchMapping("/update/{boardId}")
    public ApiResponse<Void> updateBoard(
            @RequestBody @Valid BoardEditRequest request,
            @PathVariable("boardId") Long boardId) {

        boardService.updateBoard(request.toServiceRequest(), boardId);

        return ApiResponse.ok();

    }

    /**
     * 게시판을 삭제.
     *
     * @param boardId 삭제할 게시판의 ID
     * @return 성공 여부를 반환.
     */
    @DeleteMapping("/delete/{boardId}")
    public ApiResponse<Void> deleteBoard(@PathVariable("boardId") Long boardId) {

        boardService.deleteBoard(boardId);

        return ApiResponse.ok();

    }

    /**
     * 게시판의 게시글 목록을 요청.
     *
     * @param boardId 게시글 목록을 요청할 게시판의 ID
     * @param page 요청할 페이지 번호
     * @param size 페이지당 항목 수
     * @return 페이지네이션된 게시글 목록을 반환.
     */
    @GetMapping("/posts/{boardId}")
    public ApiResponse<Page<BoardPostResponse>> getPosts(
            @PathVariable("boardId") Long boardId,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<BoardPostResponse> posts = boardService.getPostFindAll(boardId, pageable);
        return ApiResponse.ok(posts);

    }


    /**
     * 게시글을 생성.
     *
     * @param boardId 게시글을 생성할 게시판의 ID
     * @param request 게시글 생성 요청 DTO
     * @param httpServletRequest 요청의 HttpServletRequest 객체
     * @return 성공 여부를 반환.
     */
    @PostMapping("/posts/{boardId}")
    public ApiResponse<Void> createPost(
            @PathVariable("boardId") Long boardId,
            @RequestBody @Valid BoardPostCreateRequest request,
            HttpServletRequest httpServletRequest) {

        boardService.createPost(boardId, request.toServiceRequest(), httpServletRequest);

        return ApiResponse.ok();

    }

    /**
     * 게시글 상세보기 정보를 요청.
     *
     * @param postId 요청할 게시글의 ID
     * @param httpServletRequest 요청의 HttpServletRequest 객체
     * @return 게시글의 상세 정보를 반환.
     */
    @GetMapping("/posts/details/{postId}")
    public ApiResponse<BoardPostDetailResponse> getPostDetails(
            @PathVariable("postId") Long postId,
            HttpServletRequest httpServletRequest) {

        return ApiResponse.ok(boardService.getPostDetails(postId, httpServletRequest));

    }

    /**
     * 게시글을 수정.
     *
     * @param postId 수정할 게시글의 ID
     * @param request 게시글 수정 요청 DTO
     * @return 성공 여부를 반환.
     */
    @PutMapping("/posts/edit/{postId}")
    public ApiResponse<Void> editPost(
            @PathVariable("postId") Long postId,
            @RequestBody @Valid BoardPostEditRequest request) {

        boardService.editBoardPost(postId, request.toServiceRequest());
        return ApiResponse.ok();
    }

    /**
     * 게시글을 삭제.
     *
     * @param postId 삭제할 게시글의 ID
     * @return 성공 여부를 반환.
     */
    @DeleteMapping("/posts/delete/{postId}")
    public ApiResponse<Void> deletePost(
            @PathVariable("postId") Long postId) {
        boardService.deleteBoardPost(postId);
        return ApiResponse.ok();
    }

    /**
     * 게시글에 좋아요를 추가.
     *
     * @param postId 좋아요를 추가할 게시글의 ID
     * @param httpServletRequest 요청의 HttpServletRequest 객체
     * @return 게시글의 현재 좋아요 수를 반환.
     */
    @PostMapping("/posts/likes/{postId}")
    public ApiResponse<BoardPostLikeCountResponse> likePost(
            @PathVariable("postId") Long postId,
            HttpServletRequest httpServletRequest) {

        boardService.likePost(postId, httpServletRequest);
        return ApiResponse.ok(boardService.getPostLikeCount(postId));

    }

    /**
     * 게시글을 스크랩.
     *
     * @param postId 스크랩할 게시글의 ID
     * @param httpServletRequest 요청의 HttpServletRequest 객체
     * @return 게시글의 현재 스크랩 수를 반환.
     */
    @PostMapping("/posts/scraps/{postId}")
    public ApiResponse<BoardPostScrapCountResponse> scrapPost(
            @PathVariable("postId") Long postId,
            HttpServletRequest httpServletRequest) {

        boardService.scrapPost(postId, httpServletRequest);
        return ApiResponse.ok(boardService.getPostScrapCount(postId));

    }

    /**
     * 게시글에 댓글을 추가.
     *
     * @param postId 댓글을 추가할 게시글의 ID
     * @param request 댓글 생성 요청 DTO
     * @param httpServletRequest 요청의 HttpServletRequest 객체
     * @return 게시글의 현재 댓글 수를 반환.
     */
    @PostMapping("/posts/comments/{postId}")
    public ApiResponse<BoardPostCommentCountResponse> commentPost(
            @PathVariable("postId") Long postId,
            @RequestBody @Valid BoardPostCommentRequest request,
            HttpServletRequest httpServletRequest) {

        boardService.commentPost(postId, request.toServiceRequest(), httpServletRequest);
        return ApiResponse.ok(boardService.getPostCommentCount(postId));

    }

    /**
     * 스크랩한 게시글 목록을 요청.
     *
     * @param page 요청할 페이지 번호
     * @param size 페이지당 항목 수
     * @param httpServletRequest 요청의 HttpServletRequest 객체
     * @return 페이지네이션된 스크랩한 게시글 목록을 반환.
     */
    @GetMapping("/scraps")
    public ApiResponse<Page<BoardPostScrapsResponse>> scrapBoardPosts(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            HttpServletRequest httpServletRequest) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<BoardPostScrapsResponse> scrapPosts = boardService.getPostScraps(httpServletRequest, pageable);

        return ApiResponse.ok(scrapPosts);

    }

    /**
     * 베스트 게시판 목록을 요청.
     *
     * @return 베스트 게시판에 포함된 게시글 목록을 반환.
     */
    @GetMapping("/best")
    public ApiResponse<List<BoardBestPostResponse>> getBestBoardPosts() {

        return ApiResponse.ok(boardService.getBestBoardPosts());

    }

    /**
     * 게시판을 검색.
     *
     * @param request 검색 요청 DTO
     * @return 검색된 게시판 목록을 반환.
     */
    @PostMapping("/search")
    public ApiResponse<List<BoardTitleResponse>> getSearchBoards(
            @RequestBody @Valid BoardSearchRequest request) {

        return ApiResponse.ok(boardService.getSearchBoards(request.toServiceRequest()));

    }

    /**
     * 게시글을 검색.
     *
     * @param request 검색 요청 DTO
     * @param page 요청할 페이지 번호
     * @param size 페이지당 항목 수
     * @return 페이지네이션된 검색된 게시글 목록을 반환.
     */
    @PostMapping("/posts/search")
    public ApiResponse<Page<BoardPostResponse>> getSearchPosts(
            @RequestBody @Valid BoardSearchRequest request,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<BoardPostResponse> searchPosts = boardService.getSearchBoardPosts(request.toServiceRequest(), pageable);

        return ApiResponse.ok(searchPosts);

    }

}
