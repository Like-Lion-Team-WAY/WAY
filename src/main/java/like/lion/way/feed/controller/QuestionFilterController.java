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

    /**
     * 공통된 로그인 부분 설정 (중복된 부분을 메서드로 추출)
     * @param user 질문 페이지의 소유자
     */
    private void setCommonModelAttributes(Model model,
                                          User user,
                                          HttpServletRequest request) {

        User loginUser = userUtil.getLoginUser(request);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("user", user);
    }

    /**
     * 질문 필터링
     * @param user 질문 페이지의 소유자
     * @param filter 질문 필터
     */
    private void setFilteredQuestions(Model model,
                                      User user,
                                      Predicate<Question> filter,
                                      HttpServletRequest request) {

        model.addAttribute("question", questionService.getQuestionByAnswerer(user, request).stream()
                .filter(filter)
                .sorted(Comparator.comparing(Question::getQuestionDate))
                .collect(Collectors.toList()));
    }

    /**
     * 거절 질문들
     */
    @GetMapping("/questions/rejected")
    public String rejectedQuestion(Model model,
                                   HttpServletRequest request) {

        User loginUser = userUtil.getLoginUser(request);
        setCommonModelAttributes(model, loginUser, request);
        setFilteredQuestions(model, loginUser, Question::getQuestionRejected, request);
        return "pages/feed/rejectedQuestionPage";
    }

    /**
     * 새로운 질문들 (답변 안 된걸 기준)
     * @param userId 질문 페이지의 소유자 Id
     */
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

    /**
     * 답변된 질문들
     * @param userId 질문 페이지의 소유자 Id
     */
    @GetMapping("/questions/reply/{userId}")
    public String showReplyQuestion(@PathVariable("userId") Long userId,
                                    Model model,
                                    HttpServletRequest request) {

        User user = userService.findByUserId(userId);
        setCommonModelAttributes(model, user, request);
        setFilteredQuestions(model, user, q -> !q.getQuestionRejected() && q.getAnswer() != null, request);
        return "pages/feed/filterQuestionPage";
    }

    /**
     * 보낸 질문들
     * @param userId 질문 페이지의 소유자 Id
     */
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
