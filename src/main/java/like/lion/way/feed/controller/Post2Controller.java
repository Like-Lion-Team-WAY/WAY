package like.lion.way.feed.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import like.lion.way.feed.domain.Post;
import like.lion.way.feed.domain.dto.PostDto;
import like.lion.way.feed.service.PostService;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
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

    @Value("${image.upload.dir}")
    private String uploadDir;

    // 로그인한 사용자 정보 조회
    private User getLoginUser(HttpServletRequest request) {
        String token = jwtUtil.getCookieValue(request, "accessToken");
        if (token == null || token.isEmpty()) {
            return null; // 토큰이 없으면 null 반환
        }
        Long loginId = jwtUtil.getUserIdFromToken(token);
        return userService.findByUserId(loginId);
    }

    @PostMapping("/posts/pin/{postId}")
    public String pinPost(@PathVariable("postId") Long postId) {
        postService.pinPost(postId);
        return "redirect:/posts";
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
}
