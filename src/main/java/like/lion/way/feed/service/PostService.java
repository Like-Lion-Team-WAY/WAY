package like.lion.way.feed.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import like.lion.way.feed.domain.Post;
import like.lion.way.feed.domain.dto.PostDto;
import like.lion.way.user.domain.User;


public interface PostService {
    List<Post> getAllPosts();
    List<Post> getAllPosts(HttpServletRequest request);
    Post getPostById(Long id);

    Post updatePost(Long id, String title, String content);

    void deletePost(Long id);

    List<Post> getPostByUser(User user);
    List<Post> getPostByUser(User user,HttpServletRequest request);
    Post pinPost(Long postId);

    Post savePost(PostDto postDto, String key, User user);
}
