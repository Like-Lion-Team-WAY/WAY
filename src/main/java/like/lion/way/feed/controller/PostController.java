package like.lion.way.feed.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import like.lion.way.feed.domain.Post;
import like.lion.way.feed.domain.Question;
import like.lion.way.feed.domain.dto.PostDto;
import like.lion.way.feed.service.PostBoxService;
import like.lion.way.feed.service.PostService;
import like.lion.way.feed.service.QuestionService;
import like.lion.way.feed.util.UserUtil;
import like.lion.way.file.service.S3Service;
import like.lion.way.user.domain.Block;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.BlockService;
import like.lion.way.user.service.FollowService;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final QuestionService questionService;
    private final FollowService followService;
    private final BlockService blockService;
    private final UserUtil userUtil;
    private final S3Service s3Service;
    private final PostBoxService postBoxService;

    /**
     * 게시글 목록 (자기 자신의 피드)
     */
    @GetMapping("/posts")
    public String getPosts(Model model, HttpServletRequest request) {

        return getPostsForUser(null, model, request);
    }

    /**
     * 게시글 목록 (다른 사용자의 피드)
     *  @param username 특정 사용자의 피드를 보기 위해 사용하는 사용자 이름
     */
    @GetMapping("/posts/{username}")
    public String getPostsByUsername(@PathVariable("username") String username,
                                     Model model,
                                     HttpServletRequest request) {

        return getPostsForUser(username, model, request);
    }

    /**
     * 게시글 고정 (핀)
     * @param postId 고정하고 싶은 게시글 Id
     */
    @PostMapping("/posts/pin/{postId}")
    public String pinPost(@PathVariable("postId") Long postId) {

        postService.pinPost(postId);
        return "redirect:/posts";
    }

    /**
     * 게시글 작성(등록)
     * @param postDto 게시글 정보
     * @param file 이미지 파일
     */
    @PostMapping("/posts/create")
    public String savePost(PostDto postDto,
                           @RequestPart(value = "image") MultipartFile file,
                           HttpServletRequest request) {

        User user = userUtil.getLoginUser(request);

        if (file.isEmpty()) {
            postService.savePost(postDto, null, user);
        } else {
            String key = s3Service.uploadFile(file);
            postService.savePost(postDto, key, user);
        }
        return "redirect:/posts";
    }

    /**
     * 게시글 생성 페이지
     */
    @GetMapping("/posts/create")
    public String createPost() {
        return "pages/feed/feedCreate";
    }

    /**
     * 게시글 상세 페이지
     * @param postId 자세히 보고 싶은 게시글의 Id
     */
    @GetMapping("/posts/detail/{postId}")
    public String showDetailPost(@PathVariable("postId") Long postId,
                                 Model model,
                                 HttpServletRequest request) {

        Post post = postService.getPostById(postId);
        model.addAttribute("post", post);

        model.addAttribute("postBox", postBoxService.getPostBoxByPostId(post).size());

        User loginUser = userUtil.getLoginUser(request);
        model.addAttribute("loginUser", loginUser);
        return "pages/feed/detailFeed";
    }

    /**
     * 사용자 피드에 뜨는 정보 (게시글, 질문)
     * @param username 자신의 피드 또는 다른 사용자의 피드를 보기 위해 사용하는 사용자 이름
     */
    private String getPostsForUser(String username,
                                   Model model,
                                   HttpServletRequest request) {

        User user = (username != null) ? userService.findByUsername(username) : userUtil.getLoginUser(request);

        if (user == null) {
            log.error("사용자 찾을 수 없음", (username != null) ? "이름: " + username : "로그인 돼 있지 않음");
            return "redirect:/posts";
        }

        if (isUserBlocked(user, request)) {
            return "redirect:/main";
        }

        //팔로워, 팔로잉, 로그인 유저 정보
        model.addAttribute("followers", followService.getFollowerList(user).size());
        model.addAttribute("followings", followService.getFollowingList(user).size());
        model.addAttribute("loginUser", userUtil.getLoginUser(request));
        model.addAttribute("isBluecheck", userService.isBlueCheck(user.getUsername()));

        populateModelWithUserPostsAndQuestions(model, user, request);

        return "pages/feed/userFeed";
    }

    /**
     * 사용자 차단 여부 확인
     * @param user 사용자
     */
    private boolean isUserBlocked(User user,
                                  HttpServletRequest request) {

        User loginUser = userUtil.getLoginUser(request);
        if (loginUser != null) {
            Block block = blockService.findByUser(user, request);
            return block != null;
        }
        return false;
    }

    /**
     * 사용자 게시글, 질문 정보
     * @param user 사용자
     */
    private void populateModelWithUserPostsAndQuestions(Model model,
                                                        User user,
                                                        HttpServletRequest request) {

        List<Post> posts = postService.getPostByUser(user, request);
        List<Question> questions = questionService.getQuestionByAnswerer(user, request);

        model.addAttribute("user", user);
        model.addAttribute("posts", filterNonPinnedPosts(posts));
        model.addAttribute("pinPosts", filterPinnedPosts(posts));
        model.addAttribute("questions", filterAnsweredQuestions(questions));
        model.addAttribute("rejectedQuestions", countRejectedQuestions(questions));
        model.addAttribute("newQuestions", countNewQuestions(questions));
        model.addAttribute("replyQuestions", countReplyQuestions(questions));
        model.addAttribute("sendQuestions", questionService.getQuestionByQuestioner(user).size());
    }

    /**
     * 고정되지 않은 게시글 필터링
     * @param posts 게시글
     */
    private List<Post> filterNonPinnedPosts(List<Post> posts) {
        return posts.stream().filter(p -> !p.isPostPinStatus())
                .sorted(Comparator.comparing(Post::getPostCreatedAt))
                .toList();
    }

    /**
     * 고정된 게시글 필터링
     * @param posts 게시글
     */
    private List<Post> filterPinnedPosts(List<Post> posts) {
        return posts.stream()
                .filter(Post::isPostPinStatus)
                .sorted(Comparator.comparing(Post::getPostCreatedAt))
                .toList();
    }

    /**
     * 답변된 질문 필터링
     * @param questions 질문
     */
    private List<Question> filterAnsweredQuestions(List<Question> questions) {
        return questions.stream()
                .filter(q -> !q.getQuestionRejected() && q.getAnswer() != null)
                .sorted(Comparator.comparing(Question::getQuestionDate))
                .toList();
    }

    /**
     * 거절된 질문 수
     * @param questions 질문
     */
    private int countRejectedQuestions(List<Question> questions) {
        return (int) questions.stream().filter(Question::getQuestionRejected).count();
    }

    /**
     * 답변되지 않은 질문 수
     * @param questions 질문
     */
    private int countNewQuestions(List<Question> questions) {
        return (int) questions.stream().filter(q -> !q.getQuestionRejected() && q.getAnswer() == null).count();
    }

    /**
     * 답변된 질문 수
     * @param questions 질문
     */
    private int countReplyQuestions(List<Question> questions) {
        return (int) questions.stream().filter(q -> !q.getQuestionRejected() && q.getAnswer() != null).count();
    }
}
