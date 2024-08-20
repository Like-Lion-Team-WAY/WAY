package like.lion.way.feed.service;

import java.util.List;
import like.lion.way.feed.domain.Post;
import like.lion.way.feed.domain.dto.PostDto;
import like.lion.way.user.domain.User;


public interface PostService {
    List<Post> getAllPosts();

    Post getPostById(Long id);

    Post updatePost(Long id, String title, String content);

    void deletePost(Long id);

    List<Post> getPostByUser(User user);

    Post pinPost(Long postId);

    Post savePost(PostDto postDto, String key, User user);
}
