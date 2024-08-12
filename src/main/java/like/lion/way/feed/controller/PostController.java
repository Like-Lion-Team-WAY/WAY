package like.lion.way.feed.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import like.lion.way.feed.domain.Post;
import like.lion.way.feed.service.PostService;
import like.lion.way.feed.service.QuestionService;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.domain.Follow;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.FollowService;
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
    private final FollowService followService;
    private final JwtUtil jwtUtil;

    // 로그인한 사용자 정보 조회
    private User getLoginUser(HttpServletRequest request) {
        String token = jwtUtil.getCookieValue(request, "accessToken");
        if (token == null || token.isEmpty()) {
            System.out.println("Token is null or empty");
            return null;
        }
        Long loginId = jwtUtil.getUserIdFromToken(token);
        if (loginId == null) {
            System.out.println("Login ID is null");
            return null;
        }
        System.out.println(loginId);
        return userService.findByUserId(loginId);
    }

    // 공통된 Model 설정 메서드(새 질문, 답변 완료, 보낸 질문, 거절한 질문)
    private void setCommonModelFilterAttributes(Model model, User user) {
        if (user == null) {
            log.error("User object is null");
            model.addAttribute("posts", null);
            model.addAttribute("pinPosts", null);
            model.addAttribute("rejectedQuestions", 0);
            model.addAttribute("newQuestions", 0);
            model.addAttribute("replyQuestions", 0);
            model.addAttribute("sendQuestions", 0);
        } else {
            model.addAttribute("user", user);
            log.info("user::::" + user.getUsername());

            List<Post> posts = postService.getPostByUser(user);
            if (posts != null) {
                model.addAttribute("posts", posts.stream().filter(p -> !p.isPostPinStatus()).toList());
                model.addAttribute("pinPosts", posts.stream().filter(Post::isPostPinStatus).toList());
            } else {
                model.addAttribute("posts", null);
                model.addAttribute("pinPosts", null);
            }

            model.addAttribute("rejectedQuestions",
                    questionService.getQuestionByAnswerer(user).stream()
                            .filter(q -> Boolean.TRUE.equals(q.getQuestionRejected())).toList().size());

            model.addAttribute("newQuestions",
                    questionService.getQuestionByAnswerer(user).stream()
                            .filter(q -> Boolean.FALSE.equals(q.getQuestionRejected()) && q.getAnswer() == null)
                            .toList().size());

            model.addAttribute("replyQuestions",
                    questionService.getQuestionByAnswerer(user).stream()
                            .filter(q -> Boolean.FALSE.equals(q.getQuestionRejected()) && q.getAnswer() != null)
                            .toList().size());

            model.addAttribute("sendQuestions",
                    questionService.getQuestionByQuestioner(user).stream().toList().size());
        }
    }

    // 내 게시판 보여주기
    @GetMapping("/posts")
    public String getPosts(Model model, HttpServletRequest request){
        User user = getLoginUser(request);
        model.addAttribute("followers", followService.getFollowerList(user).size());
        model.addAttribute("followings", followService.getFollowingList(user).size());
        setCommonModelFilterAttributes(model, user);
        model.addAttribute("loginUser", user);
        return "/pages/feed/userFeed";
    }

    // userId에 해당하는 게시판 보여주기
    @GetMapping("/posts/{username}")
    public String getPostsByUserId(@PathVariable("username") String username, Model model, HttpServletRequest request) {
        log.info("username::::" + username);
        // username으로 User 객체 조회
        User user = userService.findByUsername(username);
        if (user == null) {
            log.error("User with username {} not found", username);
            return "redirect:/posts"; // 유저가 없으면 게시판 목록 페이지로 리다이렉트
        }

//         팔로워, 팔로잉 정보 추가
        model.addAttribute("followers", followService.getFollowerList(user).size());
        model.addAttribute("followings", followService.getFollowingList(user).size());
//        request 로 가져와야 되는데 비로그인 같은 경우는 못 가져옴
        //임시로
        model.addAttribute("followers", 0);
        model.addAttribute("followings", 0);
        // 로그인 사용자 정보 조회
        User loginUser = getLoginUser(request);
        if(loginUser == null){
            System.out.println("loginUser is null");
        }
        model.addAttribute("loginUser", loginUser);

        // 공통 모델 속성 설정
        setCommonModelFilterAttributes(model, user);

        return "/pages/feed/userFeed";
    }
}
