package like.lion.way.feed.service;

public interface LikeService {
    void likePost(Long postId, Long userId);

    void likeQuestion(Long questionId, Long userId);
}
