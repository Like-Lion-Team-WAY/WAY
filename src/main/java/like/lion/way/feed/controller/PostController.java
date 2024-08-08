package like.lion.way.feed.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import like.lion.way.feed.domain.Post;
import like.lion.way.feed.domain.PostBox;
import like.lion.way.feed.domain.dto.PostDto;
import like.lion.way.feed.service.PostBoxService;
import like.lion.way.feed.service.PostService;
import like.lion.way.feed.service.QuestionService;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final QuestionService questionService;
    private final JwtUtil jwtUtil;
    private final PostBoxService postBoxService;

    @Value("${image.upload.dir}")
    private String uploadDir;


    // 로그인한 사용자 정보 조회
    private User getLoginUser(HttpServletRequest request) {
        String token = jwtUtil.getCookieValue(request, "accessToken");
        Long loginId = jwtUtil.getUserIdFromToken(token);
        return userService.findByUserId(loginId);
    }

    // 공통된 Model 설정 메서드(새 질문, 답변 완료, 보낸 질문, 거절한 질문)
    private void setCommonModelFilterAttributes(Model model, User user) {

        model.addAttribute("user", user);
        model.addAttribute("posts", postService.getPostByUser(user));
        model.addAttribute("rejectedQuestions", questionService.getQuestionByAnswerer(user)
                .stream()
                .filter(q -> q.getQuestionRejected())
                .toList().size());
        model.addAttribute("newQuestions", questionService.getQuestionByAnswerer(user)
                .stream()
                .filter(q -> !q.getQuestionRejected() && q.getAnswer() == null)
                .toList().size());
        model.addAttribute("replyQuestions", questionService.getQuestionByAnswerer(user)
                .stream()
                .filter(q -> !q.getQuestionRejected() && q.getAnswer() != null)
                .toList().size());
        model.addAttribute("sendQuestions", questionService.getQuestionByQuestioner(user)
                .stream()
                .toList().size());
    }

    // 내 게시판 보여주기
    @GetMapping("/posts")
    public String getPosts(Model model, HttpServletRequest request){
        User user = getLoginUser(request);
        setCommonModelFilterAttributes(model, user);
        model.addAttribute("loginUser", user);
        return "/pages/feed/userFeed";
    }

    // userId에 해당하는 게시판 보여주기
    @GetMapping("/posts/{username}")
    public String getPostsByUserId(@PathVariable("username") String username, Model model, HttpServletRequest request) {
        User loginUser = getLoginUser(request);
        User user = userService.findByUsername(username);
        model.addAttribute("loginUser", loginUser);
        setCommonModelFilterAttributes(model, user);
        return "/pages/feed/userFeed";
    }

    // 게시판 생성 페이지로 넘어감
    @GetMapping("/posts/create")
    public String createPost() {
        return "/pages/feed/feedCreate";
    }

    // 게시판 생성
    @PostMapping("/posts/create")
    public String savePost(PostDto postDto, @RequestPart(value = "image") MultipartFile file, HttpServletRequest request){
        Post post = new Post();
        post.setPostTitle(postDto.getTitle()); // 제목
        post.setPostContent(postDto.getContent()); // 내용

        // 이미지 파일 저장
        if (!file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                String filePath = uploadDir + File.separator + fileName;
                File dest = new File(filePath);
                file.transferTo(dest);
                post.setPostImageUrl(fileName); // 웹에서 접근할 경로
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        User user = getLoginUser(request);
        log.info("username::::" + user.getUsername());
        post.setUser(user); // 작성자 설정
        post.setPostCreatedAt(LocalDateTime.now()); // 작성일
        post.setPostLike(0); // 좋아요 수
        postService.savePost(post);
        return "redirect:/posts";
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
