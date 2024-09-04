package like.lion.way.board.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    /**
     * 게시판 목록 페이지를 반환.
     *
     * @return 게시판 목록 페이지의 뷰 이름을 반환.
     */
    @GetMapping
    public String showBoardList() {

        return "pages/boards/boardForm";

    }

    /**
     * 게시판 생성 페이지를 반환.
     *
     * @return 게시판 생성 페이지의 뷰 이름을 반환.
     */
    @GetMapping("/create")
    public String showCreateBoardForm() {

        return "pages/boards/boardCreateForm";

    }

    /**
     * 게시판 설정 페이지를 반환.
     *
     * @return 게시판 설정 페이지의 뷰 이름을 반환.
     */
    @GetMapping("/setting/{boardId}")
    public String showBoardSettingForm() {

        return "pages/boards/boardSettingForm";

    }

    /**
     * 게시판 목록 페이지를 반환.
     *
     * @return 게시판 목록 페이지의 뷰 이름을 반환.
     */
    @GetMapping("/{boardId}")
    public String  showPostForm() {

        return "pages/boards/postForm";

    }


    /**
     * 게시글 생성 페이지를 반환.
     *
     * @return 게시글 생성 페이지의 뷰 이름을 반환.
     */
    @GetMapping("/posts/create/{boardId}")
    public String showPostCreateForm() {

        return "pages/boards/postCreateForm";

    }


    /**
     * 게시글 상세보기 페이지를 반환.
     *
     * @return 게시글 상세보기 페이지의 뷰 이름을 반환.
     */
    @GetMapping("/posts/{boardId}/{postId}")
    public String showPostDetail() {

        return "pages/boards/postDetailForm";

    }

    /**
     * 게시글 수정 페이지를 반환.
     *
     * @return 게시글 수정 페이지의 뷰 이름을 반환.
     */
    @GetMapping("/posts/edit/{boardId}/{postId}")
    public String showPostEditForm() {

        return "pages/boards/postEditForm";

    }

    /**
     * 내 스크랩 페이지를 반환.
     *
     * @return 내 스크랩 페이지의 뷰 이름을 반환.
     */
    @GetMapping("/scraps")
    public String showScrapForm() {

        return "pages/boards/scrapForm";

    }

    /**
     * 베스트 게시글 페이지를 반환.
     *
     * @return 베스트 게시글 페이지의 뷰 이름을 반환.
     */
    @GetMapping("/best")
    public String showBestPosts() {

        return "pages/boards/bestPost";

    }

}