package like.lion.way.feed.service.imp;

import like.lion.way.feed.domain.Post;
import like.lion.way.feed.domain.PostBox;
import like.lion.way.feed.repository.PostBoxRepository;
import like.lion.way.feed.service.PostBoxService;
import like.lion.way.feed.service.PostService;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostBoxServiceImpl implements PostBoxService {
    private final PostService postService;
    private final PostBoxRepository postBoxRepository;
    private final UserService userService;

    @Override
    public PostBox archievePost(Long postId) {
        Post post = postService.getPostById(postId);
        PostBox postBox = new PostBox();
        postBox.setPost(post);
        //임의의 유저값으로 설정
        log.info("user: {}", postBox.getUser());
        return postBoxRepository.save(postBox);
    }
}
