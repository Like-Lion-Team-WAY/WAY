package like.lion.way.feed.controller;

import like.lion.way.feed.service.PostBoxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostBoxController {
    private final PostBoxService postBoxService;
    @PostMapping("/posts/archieve/{postId}")
    public String archievePost(@PathVariable("postId") Long postId){
        log.info("postId: {}", postId);
        postBoxService.archievePost(postId);
        return "redirect:/posts";
    }
}
