package like.lion.way.feed.service;

import like.lion.way.feed.domain.PostBox;
import org.springframework.stereotype.Service;

@Service
public interface PostBoxService {
    PostBox archievePost(Long postId);
}
