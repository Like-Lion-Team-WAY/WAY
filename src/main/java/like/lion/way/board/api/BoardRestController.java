package like.lion.way.board.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
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
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
@Slf4j
public class BoardRestController {

    private final BoardService boardService;

    // 게시판 목록 요청
    @GetMapping
    public ApiResponse<Page<BoardTitleResponse>> getBoardList(
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        Pageable pageable = PageRequest.of(page - 1, size);

        return ApiResponse.ok(boardService.getBoardFindAll(pageable));

    }

    // 게시판 정보(제목, id) 요청 + 작성자 일치 여부
    @GetMapping("/{boardId}")
    public ApiResponse<BoardTitleResponse> getBoardTitle(@PathVariable("boardId") Long boardId,
                                                         HttpServletRequest httpServletRequest) {

        return ApiResponse.ok(boardService.getBoardTitle(boardId, httpServletRequest));

    }

    // 게시판 생성
    @PostMapping("/create")
    public ApiResponse<Void> createBoard(@RequestBody @Valid BoardCreateRequest request,
                                         HttpServletRequest httpServletRequest) {

        boardService.createBoard(request.toServiceRequest(), httpServletRequest);
        return ApiResponse.ok();

    }

    // 게시판 수정
    @PatchMapping("/update/{boardId}")
    public ApiResponse<Void> updateBoard(
            @RequestBody @Valid BoardEditRequest request,
            @PathVariable("boardId") Long boardId) {

        boardService.updateBoard(request.toServiceRequest(), boardId);

        return ApiResponse.ok();

    }

    // 게시판 삭제
    @DeleteMapping("/delete/{boardId}")
    public ApiResponse<Void> deleteBoard(@PathVariable("boardId") Long boardId) {

        boardService.deleteBoard(boardId);

        return ApiResponse.ok();

    }

    // 게시글 목록 요청
    @GetMapping("/posts/{boardId}")
    public ApiResponse<Page<BoardPostResponse>> getPosts(
            @PathVariable("boardId") Long boardId,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<BoardPostResponse> posts = boardService.getPostFindAll(boardId, pageable);
        return ApiResponse.ok(posts);

    }


    // 게시글 생성
    @PostMapping("/posts/{boardId}")
    public ApiResponse<Void> createPost(
            @PathVariable("boardId") Long boardId,
            @RequestBody @Valid BoardPostCreateRequest request,
            HttpServletRequest httpServletRequest) {

        boardService.createPost(boardId, request.toServiceRequest(), httpServletRequest);

        return ApiResponse.ok();

    }

    // 게시글 상세보기 정보 요청
    @GetMapping("/posts/details/{postId}")
    public ApiResponse<BoardPostDetailResponse> getPostDetails(
            @PathVariable("postId") Long postId,
            HttpServletRequest httpServletRequest) {

        return ApiResponse.ok(boardService.getPostDetails(postId, httpServletRequest));

    }

    // 게시글 수정
    @PutMapping("/posts/edit/{postId}")
    public ApiResponse<Void> editPost(
            @PathVariable("postId") Long postId,
            @RequestBody @Valid BoardPostEditRequest request) {

        boardService.editBoardPost(postId, request.toServiceRequest());
        return ApiResponse.ok();
    }

    // 게시글 삭제
    @DeleteMapping("/posts/delete/{postId}")
    public ApiResponse<Void> deletePost(
            @PathVariable("postId") Long postId) {
        boardService.deleteBoardPost(postId);
        return ApiResponse.ok();
    }

    // 게시글 좋아요
    @PostMapping("/posts/likes/{postId}")
    public ApiResponse<BoardPostLikeCountResponse> likePost(
            @PathVariable("postId") Long postId,
            HttpServletRequest httpServletRequest) {

        boardService.likePost(postId, httpServletRequest);
        return ApiResponse.ok(boardService.getPostLikeCount(postId));

    }

    // 게시글 스크랩
    @PostMapping("/posts/scraps/{postId}")
    public ApiResponse<BoardPostScrapCountResponse> scrapPost(
            @PathVariable("postId") Long postId,
            HttpServletRequest httpServletRequest) {

        boardService.scrapPost(postId, httpServletRequest);
        return ApiResponse.ok(boardService.getPostScrapCount(postId));

    }

    // 댓글 작성
    @PostMapping("/posts/comments/{postId}")
    public ApiResponse<BoardPostCommentCountResponse> commentPost(
            @PathVariable("postId") Long postId,
            @RequestBody @Valid BoardPostCommentRequest request,
            HttpServletRequest httpServletRequest) {

        boardService.commentPost(postId, request.toServiceRequest(), httpServletRequest);
        return ApiResponse.ok(boardService.getPostCommentCount(postId));

    }

    // 스크랩 게시글 요청
    @GetMapping("/scraps")
    public ApiResponse<Page<BoardPostScrapsResponse>> scrapBoardPosts(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            HttpServletRequest httpServletRequest) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<BoardPostScrapsResponse> scrapPosts = boardService.getPostScraps(httpServletRequest, pageable);

        return ApiResponse.ok(scrapPosts);

    }

    // 베스트 게시판 목록 요청
    @GetMapping("/best")
    public ApiResponse<List<BoardBestPostResponse>> getBestBoardPosts() {

        return ApiResponse.ok(boardService.getBestBoardPosts());

    }

    // 게시판 검색 기능
    @PostMapping("/search")
    public ApiResponse<List<BoardTitleResponse>> getSearchBoards(
            @RequestBody @Valid BoardSearchRequest request) {

        return ApiResponse.ok(boardService.getSearchBoards(request.toServiceRequest()));

    }

    // 게시글 검색 기능
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
