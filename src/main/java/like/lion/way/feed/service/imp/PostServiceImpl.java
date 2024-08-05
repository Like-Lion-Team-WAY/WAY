package like.lion.way.feed.service.imp;

import java.time.LocalDateTime;
import java.util.List;
import like.lion.way.feed.domain.Post;
import like.lion.way.feed.repository.PostRepository;
import like.lion.way.feed.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    @Transactional
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

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


}
