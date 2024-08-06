package like.lion.way.board.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import like.lion.way.board.api.request.BoardCreateRequest;
import like.lion.way.board.api.request.BoardEditRequest;
import like.lion.way.board.application.BoardService;
import like.lion.way.board.api.request.BoardPostCreateRequest;
import like.lion.way.board.application.response.BoardPostResponse;
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

    @PatchMapping("/update/{boardName}")
    public ApiResponse<Void> updateBoard(
            @RequestBody @Valid BoardEditRequest request,
            @PathVariable("boardName") String boardName) {

        boardService.updateBoard(request.toServiceRequest(), boardName);

        return ApiResponse.ok();

    }

    @DeleteMapping("/delete/{boardName}")
    public ApiResponse<Void> deleteBoard(@PathVariable("boardName") String boardName) {

        boardService.deleteBoard(boardName);

        return ApiResponse.ok();

    }

    @GetMapping("/posts/{boardName}")
    public ApiResponse<Page<BoardPostResponse>> getPosts(
            @PathVariable("boardName") String name,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<BoardPostResponse> posts = boardService.getPostFindAll(name, pageable);
        return ApiResponse.ok(posts);
    }


    @PostMapping("/posts/{boardName}")
    public ApiResponse<Void> createPost(
            @PathVariable("boardName") String boardName,
            @RequestBody @Valid BoardPostCreateRequest request,
            HttpServletRequest httpServletRequest) {

        boardService.createPost(boardName, request.toServiceRequest(), httpServletRequest);
        return ApiResponse.ok();

    }

}
