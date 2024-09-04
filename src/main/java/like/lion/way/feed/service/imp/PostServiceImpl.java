package like.lion.way.feed.service.imp;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import like.lion.way.feed.domain.Post;
import like.lion.way.feed.domain.dto.PostDto;
import like.lion.way.feed.repository.PostRepository;
import like.lion.way.feed.service.PostService;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.BlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final BlockService blockService;
    private final JwtUtil jwtUtil;

    @Value("${image.upload.dir}")
    private String uploadDir;

    /**
     * 게시글 전체 조회
     */
    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByPostCreatedAtAsc();
    }

    /**
     * 게시글 전체 조회 (차단된 사용자 필터링)
     * @param request
     */
    @Override
    public List<Post> getAllPosts(HttpServletRequest request) {
        List<Post> post = postRepository.findAllByOrderByPostCreatedAtAsc();
        return (List<Post>) blockService.blockFilter(post, request);
    }

    /**
     * 게시글 조회
     * @param id
     */
    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    /**
     * 게시글 수정
     * @param id
     * @param title
     * @param content
     */
    @Override
    @Transactional
    public Post updatePost(Long id, String title, String content) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        post.setPostTitle(title);
        post.setPostContent(content);
        post.setPostCreatedAt(LocalDateTime.now()); //수정 시간을 반영할지는 고민해보자.
        return postRepository.save(post);
    }

    /**
     * 게시글 삭제
     * @param id
     */
    @Override
    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    /**
     * 사용자별 게시글 조회
     * @param user
     */
    @Override
    public List<Post> getPostByUser(User user) {
        return postRepository.findPostByUser(user);
    }

    /**
     * 사용자별 게시글 조회 (차단된 사용자 필터링)
     * @param user
     * @param request
     */
    @Override
    public List<Post> getPostByUser(User user, HttpServletRequest request) {
        List<Post> posts = postRepository.findPostByUser(user);
        return (List<Post>) blockService.blockFilter(posts, request);
    }

    /**
     * 게시글 고정 (핀)
     * @param postId
     */
    @Override
    @Transactional
    public Post pinPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        if (post.isPostPinStatus() == true) {
            post.setPostPinStatus(false);
        } else {
            post.setPostPinStatus(true);
        }
        return postRepository.save(post);
    }

    /**
     * 게시글 작성(등록)
     * @param postDto
     * @param key
     * @param user
     */
    @Override
    @Transactional
    public Post savePost(PostDto postDto, String key, User user) {
        Post post = new Post();
        post.setPostTitle(postDto.getTitle()); // 제목
        post.setPostContent(postDto.getContent()); // 내용
        post.setPostImageUrl(key);  //이미지
        post.setUser(user); // 작성자 설정
        post.setPostCreatedAt(LocalDateTime.now()); // 작성일
        post.setPostLike(0); // 좋아요 수
        return postRepository.save(post);
    }
}
