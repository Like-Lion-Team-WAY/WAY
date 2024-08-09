package like.lion.way.feed.service;

import java.util.List;
import like.lion.way.feed.domain.Post;
import like.lion.way.feed.domain.PostBox;
import like.lion.way.user.domain.User;
import org.springframework.stereotype.Service;

@Service
public interface PostBoxService {
    PostBox archievePost(Long postId, Long userId);

    List<PostBox> getPostBoxByPostId(Post post);

    List<PostBox> getPostBoxByUserId(User user);
}
