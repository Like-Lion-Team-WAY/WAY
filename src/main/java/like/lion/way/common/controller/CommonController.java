package like.lion.way.common.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import like.lion.way.feed.domain.Post;
import like.lion.way.feed.domain.Question;
import like.lion.way.feed.service.PostService;
import like.lion.way.feed.service.QuestionService;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.domain.User;
import like.lion.way.user.dto.FollowDto;
import like.lion.way.user.service.FollowService;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommonController {
    private final PostService postService;
    private final QuestionService questionService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final FollowService followService;

    private User getLoginUser(HttpServletRequest request) {
        String token = jwtUtil.getCookieValue(request, "accessToken");
        if (token == null || token.isEmpty()) {
            log.info("Token is null or empty");
            return null;
        }
        Long loginId = jwtUtil.getUserIdFromToken(token);
        if (loginId == null) {
            log.info("Login ID is null");
            return null;
        }
        return userService.findByUserId(loginId);
    }

    @GetMapping("/main")
    public String mainView(Model model, HttpServletRequest request) {
        User loginUser = getLoginUser(request);
        if (loginUser == null) {
            //비로그인 (우선 전체 게시글, 질문)
            model.addAttribute("posts", postService.getAllPosts());
            model.addAttribute("questions", questionService.getAllQuestions());
        } else {
            //로그인 (팔로우 한 사람들 게시글, 질문)
            List<FollowDto> follows = followService.getFollowingList(loginUser);
            List<Post> posts= new ArrayList<>();
            for (FollowDto follow : follows) {
                User user = userService.findByUsername(follow.getUsername());
                posts.addAll(postService.getPostByUser(user,request));
            }
            model.addAttribute("posts", posts);

            List<Question> questions = new ArrayList<>();
            for (FollowDto follow : follows) {
                User user = userService.findByUsername(follow.getUsername());
                questions.addAll(questionService.getQuestionByAnswerer(user,request));
            }
            model.addAttribute("questions", questions.stream().filter(q -> !q.getQuestionRejected() && q.getAnswer() != null).toList());
        }
        return "pages/common/main";
    }
    @GetMapping("/")
    public String mainPage(){
        return "redirect:/main";
    }
}
