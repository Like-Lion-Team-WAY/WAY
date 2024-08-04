package like.lion.way.board.api;

import jakarta.validation.Valid;
import java.util.List;
import like.lion.way.board.api.request.BoardCreateRequest;
import like.lion.way.board.application.BoardService;
import like.lion.way.board.api.request.BoardPostCreateRequest;
import like.lion.way.board.application.response.BoardPostResponse;
import like.lion.way.board.application.response.BoardTitleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final PagedResourcesAssembler<BoardPostResponse> pagedResourcesAssembler;

    @GetMapping
    public ApiResponse<List<BoardTitleResponse>> getBoardList() {

        return ApiResponse.ok(boardService.getBoardFindAll());

    }

    @PostMapping("/create")
    public ApiResponse<Void> createBoard(@RequestBody @Valid BoardCreateRequest request) {

        String token = "테스트 토큰";
        boardService.createBoard(request.toServiceRequest(), token);
        return ApiResponse.ok();

    }

//    @PatchMapping("{boardName}")
//    public ResponseEntity<String> updateBoard(@RequestBody @Valid BoardEditRequest request, @PathVariable Long boardId) {
//
//        boardService.updateBoard(request.toServiceRequest(), boardId);
//
//        return ResponseEntity.ok("게시판 수정 성공");
//
//    }
//
//    @DeleteMapping("{boardName}")
//    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId) {
//
//        boardService.deleteBoard(boardId);
//
//        return ResponseEntity.ok("게시판 삭제 성공");
//
//    }

    @GetMapping("/posts/{boardName}")
    public ApiResponse<PagedModel<BoardPostResponse>> getPosts(
            @PathVariable("boardName") String name,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<BoardPostResponse> posts = boardService.getPostFindAll(name, pageable);
        PagedModel<BoardPostResponse> pagedModel = pagedResourcesAssembler.toModel(posts);
        return ApiResponse.ok(pagedModel);
    }


    @PostMapping("/posts/{boardName}")
    public ApiResponse<Void> createPost(
            @PathVariable("boardName") String boardName,
            @RequestBody @Valid BoardPostCreateRequest request) {

        //매개변수에 토큰 얻어오는거 필요
        String token = "test token";
        boardService.createPost(boardName, request.toServiceRequest(), token);
        return ApiResponse.ok();

    }

}
