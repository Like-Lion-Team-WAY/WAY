package like.lion.way.feed.service.imp;

import like.lion.way.feed.domain.Like;
import like.lion.way.feed.domain.Post;
import like.lion.way.feed.domain.Question;
import like.lion.way.feed.repository.LikeRepository;
import like.lion.way.feed.repository.PostRepository;
import like.lion.way.feed.repository.QuestionRepository;
import like.lion.way.feed.service.LikeService;
import like.lion.way.user.domain.User;
import like.lion.way.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final QuestionRepository questionRepository;

    /**
     * 게시글 좋아요
     * @param postId 게시글 Id
     * @param userId 게시글 좋아요를 누른 사용자 Id
     */
    @Override
    @Transactional
    public void likePost(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("invalid userId"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("invalid postId"));

        Like existingLike = likeRepository.findByUserAndPost(user, post);

        if (existingLike == null) {
            // 좋아요 추가
            Like like = new Like();
            like.setPost(post);
            like.setUser(user);
            post.setPostLike(post.getPostLike() + 1);
            postRepository.save(post);
            likeRepository.save(like);
        } else {
            // 좋아요 취소
            post.setPostLike(post.getPostLike() - 1);
            postRepository.save(post);
            likeRepository.delete(existingLike);
        }
    }

    /**
     * 질문 좋아요
     * @param questionId 질문 Id
     * @param userId 질문에 좋아요를 누른 사용자 Id
     */
    @Override
    @Transactional
    public void likeQuestion(Long questionId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("invalid userId"));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("invalid questionId"));

        Like existingLike = likeRepository.findByUserAndQuestion(user, question);

        if (existingLike == null) {
            Like like = new Like();
            like.setQuestion(question);
            like.setUser(user);
            question.setQuestionLike(question.getQuestionLike() + 1);
            questionRepository.save(question);
            likeRepository.save(like);
        } else {
            question.setQuestionLike(question.getQuestionLike() - 1);
            questionRepository.save(question);
            likeRepository.delete(existingLike);
        }

    }
}
