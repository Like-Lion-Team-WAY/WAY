package like.lion.way.feed.service;

import like.lion.way.feed.domain.Like;
import org.springframework.stereotype.Service;

@Service
public interface LikeService {
    void likePost(Long postId, Long userId);

    void likeQuestion(Long questionId, Long userId);
}
