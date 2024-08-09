package like.lion.way.feed.service.imp;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import like.lion.way.feed.domain.Post;
import like.lion.way.feed.domain.dto.PostDto;
import like.lion.way.feed.repository.PostRepository;
import like.lion.way.feed.service.PostService;
import like.lion.way.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Value("${image.upload.dir}")
    private String uploadDir;

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByPostCreatedAtAsc();
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public Post updatePost(Long id, String title, String content) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        post.setPostTitle(title);
        post.setPostContent(content);
        post.setPostCreatedAt(LocalDateTime.now()); //수정 시간을 반영할지는 고민해보자.
        return postRepository.save(post);
    }

    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public List<Post> getPostByUser(User user) {
        return postRepository.findPostByUser(user);
    }

    @Override
    public Post pinPost(Long postId) {
        Post post= postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        if(post.isPostPinStatus()==true) {
            post.setPostPinStatus(false);
        }else{
            post.setPostPinStatus(true);
        }
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public Post savePost(PostDto postDto, MultipartFile file, User user) {
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

        post.setUser(user); // 작성자 설정
        post.setPostCreatedAt(LocalDateTime.now()); // 작성일
        post.setPostLike(0); // 좋아요 수

        return postRepository.save(post);
    }
}
