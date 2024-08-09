package like.lion.way.feed.controller;

import like.lion.way.feed.service.PostBoxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostBoxController {
    private final PostBoxService postBoxService;
    //게시글 보관
    @PostMapping("/posts/archieve/{postId}")
    public String archievePost(@PathVariable("postId") Long postId,@RequestParam Long userId){
        log.info("postId: {}", postId);
        postBoxService.archievePost(postId, userId);
        return "redirect:/posts/detail/"+postId;
    }
}
