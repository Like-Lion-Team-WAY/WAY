package like.lion.way.feed.service.imp;

import like.lion.way.feed.domain.Like;
import like.lion.way.feed.domain.Post;
import like.lion.way.feed.repository.LikeRepository;
import like.lion.way.feed.repository.PostRepository;
import like.lion.way.feed.service.LikeService;
import like.lion.way.feed.service.PostService;
import like.lion.way.user.domain.User;
import like.lion.way.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostService postService;
    private final PostRepository postRepository;

    @Override
    public Like likePost(Long postId, Long userId) {
        log.info("진입!!!");
       if(likeRepository.findByUserAndPost(userRepository.findById(userId).get(), postRepository.findById(postId).get())==null) {
           Like like = new Like();
           like.setPost(postService.getPostById(postId));
           like.setUser(userRepository.findById(userId).get());
           //좋아요 갯수 수정
           Post post = postService.getPostById(postId);
           post.setPostLike(post.getPostLike()+1);
           postRepository.save(post);
           return likeRepository.save(like);
       }else{
           return null;
       }
    }
}
