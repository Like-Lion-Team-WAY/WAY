package like.lion.way.common.controller;

import jakarta.servlet.http.HttpServletRequest;
import like.lion.way.feed.service.PostService;
import like.lion.way.feed.service.QuestionService;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CommonController {
    private final PostService postService;
    private final QuestionService questionService;
    private final JwtUtil jwtUtil;

    @GetMapping("/main")
    public String mainView(Model model, HttpServletRequest request){
        model.addAttribute("posts", postService.getAllPosts());
//        model.addAttribute("questions", questionService.getAllQuestions());
        return "pages/common/main";
    }
}
