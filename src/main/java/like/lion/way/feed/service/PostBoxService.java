package like.lion.way.feed.service;

import java.util.List;
import like.lion.way.feed.domain.Post;
import like.lion.way.feed.domain.PostBox;
import org.springframework.stereotype.Service;

@Service
public interface PostBoxService {
    PostBox archievePost(Long postId, Long userId);

    List<PostBox> getPostBoxByPostId(Post post);
}
