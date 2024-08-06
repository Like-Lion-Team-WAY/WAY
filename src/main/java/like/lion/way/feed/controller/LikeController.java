package like.lion.way.feed.controller;

import like.lion.way.feed.domain.Like;
import like.lion.way.feed.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LikeController {
    private final LikeService likeService;
    //게시글에 대한 좋아요
    @PostMapping ("/posts/like/{postId}")
    public String likePost(@PathVariable("postId") Long postId, @RequestParam("userId") Long userId){
        likeService.likePost(postId, userId);
        log.info("postId like!!!: {}", postId);
        log.info("userId like!!!: {}", userId);
        return "redirect:/posts/detail/"+postId;
    }
    //질문에 대한 좋아요
}
