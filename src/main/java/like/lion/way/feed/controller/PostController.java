package like.lion.way.feed.controller;

import jakarta.servlet.http.HttpServletRequest;
import like.lion.way.feed.domain.Post;
import like.lion.way.feed.service.PostBoxService;
import like.lion.way.feed.service.PostService;
import like.lion.way.feed.service.QuestionService;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final QuestionService questionService;
    private final JwtUtil jwtUtil;


    // 로그인한 사용자 정보 조회
    private User getLoginUser(HttpServletRequest request) {
        String token = jwtUtil.getCookieValue(request, "accessToken");
        if (token == null || token.isEmpty()) {
            return null; // 토큰이 없으면 null 반환
        }
        Long loginId = jwtUtil.getUserIdFromToken(token);
        return userService.findByUserId(loginId);
    }

    // 공통된 Model 설정 메서드(새 질문, 답변 완료, 보낸 질문, 거절한 질문)
    private void setCommonModelFilterAttributes(Model model, User user) {
        if (user == null) {
            log.error("User object is null");
            // 필요한 경우, 기본 값 설정 또는 예외 처리
            model.addAttribute("posts", null);
            model.addAttribute("rejectedQuestions", 0);
            model.addAttribute("newQuestions", 0);
            model.addAttribute("replyQuestions", 0);
            model.addAttribute("sendQuestions", 0);
        } else {
            model.addAttribute("user", user);
            log.info("user::::" + user.getUsername());
            model.addAttribute("posts", postService.getPostByUser(user).stream().filter(p -> p.isPostPinStatus() == false).toList());
            model.addAttribute("pinPosts", postService.getPostByUser(user).stream().filter(p -> p.isPostPinStatus() == true).toList());

            model.addAttribute("rejectedQuestions", questionService.getQuestionByAnswerer(user)
                    .stream()
                    .filter(q -> Boolean.TRUE.equals(q.getQuestionRejected()))
                    .toList().size());
            model.addAttribute("newQuestions", questionService.getQuestionByAnswerer(user)
                    .stream()
                    .filter(q -> Boolean.FALSE.equals(q.getQuestionRejected()) && q.getAnswer() == null)
                    .toList().size());
            model.addAttribute("replyQuestions", questionService.getQuestionByAnswerer(user)
                    .stream()
                    .filter(q -> Boolean.FALSE.equals(q.getQuestionRejected()) && q.getAnswer() != null)
                    .toList().size());
            model.addAttribute("sendQuestions", questionService.getQuestionByQuestioner(user)
                    .stream()
                    .toList().size());
        }
    }


    // 내 게시판 보여주기
    @GetMapping("/posts")
    public String getPosts(Model model, HttpServletRequest request){
        User user = getLoginUser(request);
        setCommonModelFilterAttributes(model, user);
        model.addAttribute("loginUser", user);
        return "/pages/feed/userFeed";
    }

    // userId에 해당하는 게시판 보여주기
    @GetMapping("/posts/{username}")
    public String getPostsByUserId(@PathVariable("username") String username, Model model, HttpServletRequest request) {
        log.info("username::::" + username);
        User loginUser = getLoginUser(request);
        if (loginUser == null) {
            // 로그인 사용자가 없는 경우 처리
            log.error("Login user not found.");
            // 필요에 따라 예외를 던지거나 기본 페이지로 리다이렉트
            return "redirect:/user/login"; // 예를 들어 로그인 페이지로 리다이렉트
        }
        User user = userService.findByUsername(username);
        if (user == null) {
            log.error("User with username {} not found", username);
            // 필요에 따라 예외를 던지거나 기본 페이지로 리다이렉트
            return "redirect:/posts"; // 예를 들어 게시판 목록 페이지로 리다이렉트
        }
        model.addAttribute("loginUser", loginUser);
        setCommonModelFilterAttributes(model, user);
        return "/pages/feed/userFeed";
    }
}
