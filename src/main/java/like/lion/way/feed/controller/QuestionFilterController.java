package like.lion.way.feed.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Comparator;
import like.lion.way.feed.domain.Question;
import like.lion.way.feed.service.QuestionService;
import like.lion.way.feed.util.UserUtil;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class QuestionFilterController {
    private final UserService userService;
    private final QuestionService questionService;
    private final UserUtil userUtil;

    // 공통된 로그인 부분 model 설정 메서드
    private void setCommonModelAttributes(Model model,
                                          User user,
                                          HttpServletRequest request) {

        User loginUser = userUtil.getLoginUser(request);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("user", user);
    }

    // 필터된 질문 리스트를 설정하는 메서드
    private void setFilteredQuestions(Model model,
                                      User user,
                                      Predicate<Question> filter,
                                      HttpServletRequest request) {

        model.addAttribute("question", questionService.getQuestionByAnswerer(user, request).stream()
                .filter(filter)
                .sorted(Comparator.comparing(Question::getQuestionDate))
                .collect(Collectors.toList()));
    }

    // 거절 질문 리스트
    @GetMapping("/questions/rejected")
    public String rejectedQuestion(Model model,
                                   HttpServletRequest request) {

        User loginUser = userUtil.getLoginUser(request);
        setCommonModelAttributes(model, loginUser, request);
        setFilteredQuestions(model, loginUser, Question::getQuestionRejected, request);
        return "pages/feed/rejectedQuestionPage";
    }

    // 새 질문 리스트 내림차순 정렬
    @GetMapping("/questions/new/{userId}")
    public String showNewQuestion(@PathVariable("userId") Long userId,
                                  Model model,
                                  HttpServletRequest request) {

        User user = userService.findByUserId(userId);
        setCommonModelAttributes(model, user, request);
        model.addAttribute("question", questionService.getQuestionByAnswerer(user, request)
                .stream()
                .filter(q -> !q.getQuestionRejected() && q.getAnswer() == null)
                .sorted(Comparator.comparing(Question::getQuestionDate).reversed())
                .collect(Collectors.toList()));
        return "pages/feed/filterQuestionPage";
    }

    // 답변 질문 리스트
    @GetMapping("/questions/reply/{userId}")
    public String showReplyQuestion(@PathVariable("userId") Long userId,
                                    Model model,
                                    HttpServletRequest request) {

        User user = userService.findByUserId(userId);
        setCommonModelAttributes(model, user, request);
        setFilteredQuestions(model, user, q -> !q.getQuestionRejected() && q.getAnswer() != null, request);
        return "pages/feed/filterQuestionPage";
    }

    // 보낸 질문 리스트
    @GetMapping("/questions/send/{userId}")
    public String showSendQuestion(@PathVariable("userId") Long userId,
                                   Model model,
                                   HttpServletRequest request) {

        User user = userService.findByUserId(userId);
        setCommonModelAttributes(model, user, request);
        model.addAttribute("question", questionService.getQuestionByQuestioner(user));
        return "pages/feed/sendQuestion";
    }
}
