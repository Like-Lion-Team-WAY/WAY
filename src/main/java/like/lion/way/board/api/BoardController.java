package like.lion.way.board.api;

import jakarta.validation.Valid;
import java.util.List;
import like.lion.way.board.api.request.BoardCreateRequest;
import like.lion.way.board.api.request.BoardEditRequest;
import like.lion.way.board.application.BoardService;
import like.lion.way.board.application.response.BoardTitleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ModelAndView showBoardList() {

        ModelAndView modelAndView = new ModelAndView("pages/boards/board");
        List<BoardTitleResponse> boards = boardService.getBoardFindAll();
        modelAndView.addObject("boards", boards);
        return modelAndView;

    }

    @GetMapping("/create")
    public ModelAndView showCreateBoardForm() {

        return new ModelAndView("pages/boards/boardCreateForm");

    }

    @PostMapping("/create")
    public ResponseEntity<String> createBoard(@RequestBody @Valid BoardCreateRequest request) {

        String token = "테스트 토큰";
        boardService.createBoard(request.toServiceRequest(), token);
        return ResponseEntity.ok("게시판 등록 성공");

    }
    @GetMapping("/setting")
    public ModelAndView showBoardSettingForm() {

        return new ModelAndView("pages/boards/boardSettingForm");

    }

    @PatchMapping("{boardId}")
    public ResponseEntity<String> updateBoard(@RequestBody @Valid BoardEditRequest request, @PathVariable Long boardId) {

        boardService.updateBoard(request.toServiceRequest(), boardId);

        return ResponseEntity.ok("게시판 수정 성공");

    }

    @DeleteMapping("{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId) {

        boardService.deleteBoard(boardId);

        return ResponseEntity.ok("게시판 삭제 성공");

    }

}
