package like.lion.way.board.api;

import like.lion.way.board.application.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    @GetMapping
    public String showBoardList() {

        return "/pages/boards/boardForm";

    }

    @GetMapping("/create")
    public String showCreateBoardForm() {

        return "pages/boards/boardCreateForm";

    }

    @GetMapping("/setting/{boardName}")
    public String showBoardSettingForm() {

        return "pages/boards/boardSettingForm";

    }

    @GetMapping("/{boardName}")
    public String  showPostForm() {

        return "pages/boards/postForm";

    }

    @GetMapping("/posts/create/{boardName}")
    public String showPostCreateForm() {

        return "pages/boards/postCreateForm";

    }

    @GetMapping("/posts/{postTitle}")
    public String showPostDetail() {

        return "pages/boards/postDetailForm";

    }

}
