package like.lion.way.board.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import like.lion.way.board.api.request.BoardCreateRequest;
import like.lion.way.board.api.request.BoardEditRequest;
import like.lion.way.board.application.BoardService;
import like.lion.way.board.api.request.BoardPostCreateRequest;
import like.lion.way.board.application.response.BoardPostDetailResponse;
import like.lion.way.board.application.response.BoardPostLikeCountResponse;
import like.lion.way.board.application.response.BoardPostResponse;
import like.lion.way.board.application.response.BoardPostScrapCountResponse;
import like.lion.way.board.application.response.BoardTitleResponse;
import like.lion.way.board.domain.BoardPostScrap;
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

    @GetMapping
    public ApiResponse<List<BoardTitleResponse>> getBoardList() {

        return ApiResponse.ok(boardService.getBoardFindAll());

    }

    @PostMapping("/create")
    public ApiResponse<Void> createBoard(@RequestBody @Valid BoardCreateRequest request,
                                         HttpServletRequest httpRequest) {

        boardService.createBoard(request.toServiceRequest(), httpRequest);
        return ApiResponse.ok();

    }

    @PatchMapping("/update/{boardId}")
    public ApiResponse<Void> updateBoard(
            @RequestBody @Valid BoardEditRequest request,
            @PathVariable("boardId") Long boardId) {

        boardService.updateBoard(request.toServiceRequest(), boardId);

        return ApiResponse.ok();

    }

    @DeleteMapping("/delete/{boardId}")
    public ApiResponse<Void> deleteBoard(@PathVariable("boardId") Long boardId) {

        boardService.deleteBoard(boardId);

        return ApiResponse.ok();

    }

    @GetMapping("/posts/{boardId}")
    public ApiResponse<Page<BoardPostResponse>> getPosts(
            @PathVariable("boardId") Long boardId,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<BoardPostResponse> posts = boardService.getPostFindAll(boardId, pageable);
        return ApiResponse.ok(posts);
    }


    @PostMapping("/posts/{boardId}")
    public ApiResponse<Void> createPost(
            @PathVariable("boardId") Long boardId,
            @RequestBody @Valid BoardPostCreateRequest request,
            HttpServletRequest httpServletRequest) {

        log.info("포스팅 실행");

        boardService.createPost(boardId, request.toServiceRequest(), httpServletRequest);
        return ApiResponse.ok();

    }

//    @GetMapping("/posts/${boardName}/${postTitle}")
//    public ApiResponse<BoardPostDetailResponse> getPostDetails(
//            @PathVariable("boardName") String boardName,
//            @PathVariable("postTitle") String postTitle) {
//
//        return ApiResponse.ok(boardService.getPostDetails(boardName, postTitle));
//
//    }

    //get 요청 필요 없이 postDetail 로드할 때 getPostLikeCount 해서 postDetailResponse에 likes 담으면 될 것 같음
//    @GetMapping("/{postTitle}")
//    public ApiResponse<BoardPostLikeCountResponse> getBoardPostLikeCount(@PathVariable("postTitle") String postTitle) {
//
//        return ApiResponse.ok(boardService.getPostLikeCount(postTitle));
//
//    }

    @PostMapping("/posts/likes/{postId}")
    public ApiResponse<BoardPostLikeCountResponse> likePost(
            @PathVariable("postId") Long postId,
            HttpServletRequest httpServletRequest) {

        boardService.likePost(postId, httpServletRequest);
        return ApiResponse.ok(boardService.getPostLikeCount(postId));

    }

    @PostMapping("/posts/scraps/{postId}")
    public ApiResponse<BoardPostScrapCountResponse> scrapPost(
            @PathVariable("postId") Long postId,
            HttpServletRequest httpServletRequest) {

        boardService.scrapPost(postId, httpServletRequest);
        return ApiResponse.ok(boardService.getPostScrapCount(postId));

    }

}
