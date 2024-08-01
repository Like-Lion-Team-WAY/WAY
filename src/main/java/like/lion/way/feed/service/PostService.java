package like.lion.way.feed.service;

import java.util.List;
import like.lion.way.feed.domain.Post;
import org.springframework.stereotype.Service;

@Service
public interface PostService {
    Post savePost(Post post);

    List<Post> getAllPosts();

    Post getPostById(Long id);

    Post updatePost(Long id, String title, String content);

    void deletePost(Long id);
}
