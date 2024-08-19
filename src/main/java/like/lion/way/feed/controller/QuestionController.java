package like.lion.way.feed.controller;

import jakarta.servlet.http.HttpServletRequest;
import like.lion.way.feed.domain.Question;
import like.lion.way.feed.service.QuestionService;
import like.lion.way.file.service.S3Service;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@Slf4j
public class QuestionController {

    private final QuestionService questionService;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final S3Service s3Service;


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


    //내 질문 창으로만
    @GetMapping("/questions/create")
    public String createMyQuestion(Model model, HttpServletRequest request) {
        //얘는 로그인 유저 (==질문 페이지 소유자)
        User loginUser= getLoginUser(request);
        if(loginUser == null){
            model.addAttribute("loginUser", null);
        }else{
            model.addAttribute("loginUser", loginUser);
        }

        // 얘는 질문 페이지 소유자의 유저 정보
        User user = userService.findByUserId(loginUser.getUserId());
        model.addAttribute("user", user);
        model.addAttribute("question", questionService.getQuestionByAnswerer(user).stream().filter(q -> !q.getQuestionRejected() && q.getQuestionPinStatus() == false).toList());
        model.addAttribute("pinQuestion", questionService.getQuestionByAnswerer(user).stream().filter(q -> !q.getQuestionRejected() && q.getQuestionPinStatus() == true).toList());

        return "pages/feed/questionPage";
    }
    //userId 는 질문 페이지의 소유자의 userId
    //username 추가해줘야 됨
    @GetMapping("/questions/create/{userId}")
    public String createQuestion(Model model, HttpServletRequest request, @PathVariable("userId") Long userId) {
        //질문은 로그인하지 않은 사용자도 할 수 있잖아? 그걸 고려해서 다시 로직 짜보기.
        //얘는 로그인 유저
        User loginUser= getLoginUser(request);
        if(loginUser == null){
            model.addAttribute("loginUser", null);
        }else{
            model.addAttribute("loginUser", loginUser);
        }

        // 얘는 질문 페이지 소유자의 유저 정보
        User user = userService.findByUserId(userId);
        model.addAttribute("user", user);
        model.addAttribute("question", questionService.getQuestionByAnswerer(user).stream().filter(q -> !q.getQuestionRejected() && q.getQuestionPinStatus() == false).toList());
        model.addAttribute("pinQuestion", questionService.getQuestionByAnswerer(user).stream().filter(q -> !q.getQuestionRejected() && q.getQuestionPinStatus() == true).toList());

        return "pages/feed/questionPage";
    }
    //질문 등록
    //userId 질문 페이지의 소유자 아이디
    @PostMapping("/questions/create/{userId}")
    public String createQuestion(@PathVariable("userId") Long userId,
            @RequestParam("question") String question,
            @RequestParam("isAnonymous") boolean isAnonymous,
            @RequestParam(value = "image", required = false) MultipartFile image,
            HttpServletRequest request) {

        // 로그인 사용자
        User user = getLoginUser(request);
        String key= s3Service.uploadFile(image);
        //로그인한 사용자 여부 확인
        if(user == null) {
            //익명 - 비로그인 처리
            questionService.saveQuestion(userId, question, key, request);
        }else{
            //로그인 사용자 처리
            questionService.saveQuestion(user, userId, question, isAnonymous, key, request);
        }
        return "redirect:/questions/create/"+userId;
    }
    //질문 답변
    @PostMapping("/questions/answer/{questionId}")
    public String answerQuestion(@RequestParam("answer") String answer, @PathVariable("questionId") Long questionId) {
        Question question = questionService.getQuestionById(questionId);
        questionService.saveQuestion(question, answer);
        return "redirect:/questions/create";
    }
    //거절 질문 등록
    @PostMapping("/questions/enroll/rejected")
    public String enrollRejected(@RequestParam("questionId") Long questionId) {
        Question question = questionService.getQuestionById(questionId);
        questionService.rejectedQuestion(question);
        return "redirect:/questions/rejected"; //거절 질문 창으로 넘어가게
    }
}
