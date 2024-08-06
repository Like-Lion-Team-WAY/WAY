package like.lion.way.feed.controller;

import jakarta.servlet.http.HttpServletRequest;
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
public class QuestionFilterController {
    private final UserService userService;
    private final QuestionService questionService;
    private final JwtUtil jwtUtil;

    //로그인한 사용자
    private User getLoginUser(HttpServletRequest request) {
        String token = jwtUtil.getCookieValue(request, "accessToken");
        Long loginId = jwtUtil.getUserIdFromToken(token);
        return userService.findByUserId(loginId);
    }
    //거절 질문 리스트
    @GetMapping("/questions/rejected")
    public String rejectedQuestion(Model model, HttpServletRequest request) {
        //얘는 로그인 유저(==질문 페이지 소유자)
        User loginUser= getLoginUser(request);
        model.addAttribute("loginUser", loginUser);
        // 얘는 질문 페이지 소유자의 유저 정보
        User user = userService.findByUserId(loginUser.getUserId());
        model.addAttribute("user", user);
        model.addAttribute("question", questionService.getQuestionByAnswerer(user).stream().filter(q -> q.getQuestionRejected()));

        return "pages/feed/filterQuestionPage";
    }
    //새 질문 리스트
    @GetMapping("/questions/new/{userId}")
    public String showNewQuestion(@PathVariable("userId") Long userId, Model model, HttpServletRequest request){
        //얘는 로그인 유저(==질문 페이지 소유자)
        User loginUser= getLoginUser(request);
        model.addAttribute("loginUser", loginUser);
        // 얘는 질문 페이지 소유자의 유저 정보
        User user = userService.findByUserId(userId);
        model.addAttribute("user", user);
        model.addAttribute("question", questionService.getQuestionByAnswerer(user).stream().filter(q -> !q.getQuestionRejected() && q.getAnswer() == null));

        return "pages/feed/filterQuestionPage";
    }
    //답변 질문 리스트
    @GetMapping("/questions/reply/{userId}")
    public String showReplyQuestion(@PathVariable("userId") Long userId, Model model, HttpServletRequest request){
        //얘는 로그인 유저(==질문 페이지 소유자)
        User loginUser= getLoginUser(request);
        model.addAttribute("loginUser", loginUser);
        // 얘는 질문 페이지 소유자의 유저 정보
        User user = userService.findByUserId(userId);
        model.addAttribute("user", user);
        model.addAttribute("question", questionService.getQuestionByAnswerer(user).stream().filter(q -> !q.getQuestionRejected() && q.getAnswer() != null));

        return "pages/feed/filterQuestionPage";
    }
    //보낸 질문 리스트
    @GetMapping("/questions/send/{userId}")
    public String showSendQuestion(@PathVariable("userId") Long userId,Model model, HttpServletRequest request){
        //얘는 로그인 유저(==질문 페이지 소유자)
        User loginUser= getLoginUser(request);
        model.addAttribute("loginUser", loginUser);
        // 얘는 질문 페이지 소유자의 유저 정보
        User user = userService.findByUserId(userId);
        model.addAttribute("user", user);
        model.addAttribute("question", questionService.getQuestionByQuestioner(user));

        return "pages/feed/filterQuestionPage";
    }
}
