package like.lion.way.board.api;

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

        return "pages/boards/boardForm";

    }

    @GetMapping("/create")
    public String showCreateBoardForm() {

        return "pages/boards/boardCreateForm";

    }

    @GetMapping("/setting/{boardId}")
    public String showBoardSettingForm() {

        return "pages/boards/boardSettingForm";

    }

    @GetMapping("/{boardId}")
    public String  showPostForm() {

        return "pages/boards/postForm";

    }

    @GetMapping("/posts/create/{boardId}")
    public String showPostCreateForm() {

        return "pages/boards/postCreateForm";

    }

    @GetMapping("/posts/{boardId}/{postId}")
    public String showPostDetail() {

        return "pages/boards/postDetailForm";

    }

    @GetMapping("/posts/edit/{boardId}/{postId}")
    public String showPostEditForm() {

        return "pages/boards/postEditForm";

    }

    @GetMapping("/scraps")
    public String showScrapForm() {

        return "pages/boards/scrapForm";

    }

    @GetMapping("/best")
    public String showBestPosts() {

        return "pages/boards/bestPost";

    }

}
