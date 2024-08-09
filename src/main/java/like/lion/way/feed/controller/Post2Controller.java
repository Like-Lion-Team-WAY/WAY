package like.lion.way.feed.controller;

import jakarta.servlet.http.HttpServletRequest;
import like.lion.way.feed.domain.Post;
import like.lion.way.feed.domain.dto.PostDto;
import like.lion.way.feed.service.PostBoxService;
import like.lion.way.feed.service.PostService;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@Slf4j
public class Post2Controller {
    private final PostService postService;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PostBoxService postBoxService;

    // 로그인한 사용자 정보 조회
    private User getLoginUser(HttpServletRequest request) {
        String token = jwtUtil.getCookieValue(request, "accessToken");
        if (token == null || token.isEmpty()) {
            return null; // 토큰이 없으면 null 반환
        }
        Long loginId = jwtUtil.getUserIdFromToken(token);
        return userService.findByUserId(loginId);
    }
    //고정 핀 설정
    @PostMapping("/posts/pin/{postId}")
    public String pinPost(@PathVariable("postId") Long postId) {
        postService.pinPost(postId);
        return "redirect:/posts";
    }

    // 게시판 생성
    @PostMapping("/posts/create")
    public String savePost(PostDto postDto, @RequestPart(value = "image") MultipartFile file, HttpServletRequest request){
        User user = getLoginUser(request);
        postService.savePost(postDto, file, user);
        return "redirect:/posts";
    }
    // 게시판 생성 페이지로 넘어감
    @GetMapping("/posts/create")
    public String createPost() {
        return "/pages/feed/feedCreate";
    }


    // 게시판 상세 (게시판 == 피드)
    @GetMapping("/posts/detail/{postId}")
    public String showDetailPost(@PathVariable("postId") Long postId, Model model, HttpServletRequest request){
        log.info("postId:::: " + postId);
        Post post = postService.getPostById(postId);
        model.addAttribute("post", post);

        model.addAttribute("postBox", postBoxService.getPostBoxByPostId(post).size());

        User loginUser = getLoginUser(request);
        model.addAttribute("loginUser", loginUser);
        return "/pages/feed/detailFeed";
    }
}
