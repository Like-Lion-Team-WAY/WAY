package like.lion.way.feed.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import like.lion.way.feed.domain.Post;
import like.lion.way.feed.domain.dto.PostDto;
import like.lion.way.feed.service.PostService;
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

    @Value("${image.upload.dir}")
    private String uploadDir;

    //전체 게시판 보여주기 (username 로그인 구현되면 GetMapping에 포함돼야 함)
    @GetMapping("/posts")
    public String getPosts(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "/pages/feed/userFeed";
    }
    //게시판 생성 페이지로 넘어감
    @GetMapping("/posts/create")
    public String createPost() {
        return "/pages/feed/feedCreate";
    }
    //게시판 생성
    @PostMapping("/posts/create")
    public String savePost(PostDto postDto, @RequestPart(value = "image") MultipartFile file){
        Post post = new Post();
        post.setPostTitle(postDto.getTitle()); //제목
        post.setPostContent(postDto.getContent()); //내용
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

        post.setPostCreatedAt(LocalDateTime.now()); //작성일
        post.setPostLike(0); //좋아요수
        postService.savePost(post);
        return "redirect:/posts";
    }
    //게시판 상세(게시판 == 피드)
    @GetMapping("/posts/detail/{postId}")
    public String showDetailPost(@PathVariable("postId") Long postId, Model model){
        log.info("postId:::: " + postId);
        Post post = postService.getPostById(postId);
        model.addAttribute("post", post);
        return "/pages/feed/detailFeed";
    }
}