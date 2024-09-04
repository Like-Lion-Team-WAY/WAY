package like.lion.way.feed.service.imp;

import java.util.List;
import java.util.Optional;
import like.lion.way.feed.domain.Post;
import like.lion.way.feed.domain.PostBox;
import like.lion.way.feed.repository.PostBoxRepository;
import like.lion.way.feed.service.PostBoxService;
import like.lion.way.feed.service.PostService;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostBoxServiceImpl implements PostBoxService {
    private final PostService postService;
    private final PostBoxRepository postBoxRepository;
    private final UserService userService;

    /**
     * 게시글 보관
     * @param postId 게시글 Id
     * @param userId 게시글 보관을 원하는 사용자 Id
     */
    @Override
    @Transactional
    public PostBox archievePost(Long postId, Long userId) {
        User user = userService.findByUserId(userId);
        Post post = postService.getPostById(postId);

        Optional<PostBox> existingPostBox = postBoxRepository.findByUserAndPost(user, post);

        if (existingPostBox.isPresent()) {
            // 이미 보관된 게시글이면 삭제
            postBoxRepository.delete(existingPostBox.get());
            return null;
        } else {
            PostBox postBox = new PostBox();
            postBox.setPost(post);
            postBox.setUser(user);
            PostBox savedPostBox = postBoxRepository.save(postBox);
            return savedPostBox;
        }
    }

    /**
     * 게시글 보관함 조회
     * @param post 게시글
     */
    @Override
    public List<PostBox> getPostBoxByPostId(Post post) {
        return postBoxRepository.findByPost(post);
    }

    /**
     * 사용자별 게시글 보관함 조회
     * @param user 사용자
     */
    @Override
    public List<PostBox> getPostBoxByUserId(User user) {
        return postBoxRepository.findByUser(user);
    }
}
