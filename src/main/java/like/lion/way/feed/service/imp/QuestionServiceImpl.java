package like.lion.way.feed.service.imp;

import static like.lion.way.feed.util.GetUserIp.getRemoteIP;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import like.lion.way.alarm.domain.AlarmType;
import like.lion.way.alarm.event.AlarmEvent;
import like.lion.way.feed.domain.Question;
import like.lion.way.feed.repository.QuestionRepository;
import like.lion.way.feed.service.QuestionService;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.BlockService;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final BlockService blockService;

    @Value("${image.upload.dir}")
    private String uploadDir;

    /**
     * 질문 전체 조회
     */
    @Override
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    /**
     * 질문 id 에 따른 조회
     * @param questionId 질문 Id
     */
    @Override
    public Question getQuestionById(Long questionId) {
        return questionRepository.getByQuestionId(questionId);
    }

    /**
     * 질문 답변 수정
     * @param questionId 질문 Id
     * @param answer 질문 답변
     */
    @Override
    @Transactional
    public Question updateAnswer(Long questionId, String answer) {
        Question question = questionRepository.getByQuestionId(questionId);
        question.setAnswer(answer);
        question.setAnswerDate(LocalDateTime.now());
        var value = questionRepository.save(question);

        // 트랜잭션 종료 후 이벤트 발생
        AlarmEvent event = new AlarmEvent(this, AlarmType.ANSWER, value.getAnswerer(), value.getQuestioner(),
                value.getAnswerer().getUserId().toString());
        publisher.publishEvent(event);

        return value;
    }

    /**
     * 질문 답변자의 질문 데이터 조회
     * @param user 사용자
     */
    @Override
    public List<Question> getQuestionByAnswerer(User user) {
        return questionRepository.findQuestionsByAnswerer(user);
    }

    /**
     * 질문 답변자의 질문 데이터 조회 (차단된 사용자 필터링)
     * @param user 사용자
     */
    @Override
    public List<Question> getQuestionByAnswerer(User user, HttpServletRequest request) {
        List<Question> list = questionRepository.findQuestionsByAnswerer(user);
        return (List<Question>) blockService.blockFilter(list, request);
    }

    /**
     * 질문자의 질문 데이터 조회
     * @param user 사용자
     */
    @Override
    public List<Question> getQuestionByQuestioner(User user) {
        return questionRepository.findQuestionsByQuestioner(user);
    }

    /**
     * 질문 고정 (핀)
     * @param questionId 질문 Id
     */
    @Override
    @Transactional
    public Question pinQuestion(Long questionId) {
        Question question = questionRepository.getByQuestionId(questionId);
        if (question.getQuestionPinStatus() == true){
            question.setQuestionPinStatus(false);
        } else {
            question.setQuestionPinStatus(true);
        }
        return questionRepository.save(question);
    }

    /**
     * 거절 질문 등록
     * @param question 질문
     */
    @Override
    @Transactional
    public Question rejectedQuestion(Question question) {
        if (question.getQuestionRejected() == true) {
            question.setQuestionRejected(false);
        } else {
            question.setQuestionRejected(true);
        }
        return questionRepository.save(question);
    }

    /**
     * 질문 삭제
     * @param questionId 질문 Id
     */
    @Override
    @Transactional
    public void deleteQuestion(Long questionId) {
        Question question = questionRepository.getByQuestionId(questionId);
        questionRepository.delete(question);
    }

    /**
     * 질문 저장 (로그인 된 사용자)
     * @param user 질문자
     * @param userId 피드 주인 Id
     * @param question 질문 내용
     * @param isAnonymous 익명 여부
     * @param key 이미지 파일
     */
    @Override
    @Transactional
    public Question saveQuestion(User user, Long userId, String question, boolean isAnonymous, String key,
                                 HttpServletRequest request) {
        Question newQuestion = new Question();
        newQuestion.setQuestion(question);  //질문 저장
        newQuestion.setQuestionDate(LocalDateTime.now()); //질문 생성일
        newQuestion.setQuestioner(user);
        //익명 여부에 따라
        if (isAnonymous) {
            newQuestion.setIsAnonymous(true);
            newQuestion.setUserIp(getRemoteIP(request));

        } else {
            newQuestion.setIsAnonymous(false);
        }
        newQuestion.setQuestionImageUrl(key);
        User questionPageUser = userService.findByUserId(userId);
        newQuestion.setAnswerer(questionPageUser);
        newQuestion.setQuestionDeleteYN(false);
        newQuestion.setQuestionStatus(false);
        newQuestion.setQuestionPinStatus(false);
        newQuestion.setQuestionRejected(false);
        var value = questionRepository.save(newQuestion);

        // 트랜잭션 종료 후 이벤트 발생
        AlarmEvent event = new AlarmEvent(this, AlarmType.NEW_QUESTION, value.getQuestioner(), value.getAnswerer(),
                value.getAnswerer().getUserId().toString());
        publisher.publishEvent(event);

        return value;
    }

    /**
     * 질문에 대한 답변 저장
     * @param question 질문
     * @param answer 질문의 답변
     */
    @Override
    @Transactional
    public Question saveQuestion(Question question, String answer) {
        question.setAnswer(answer);
        question.setQuestionStatus(true);
        question.setAnswerDate(LocalDateTime.now());
        var value = questionRepository.save(question);

        // 트랜잭션 종료 후 이벤트 발생
        AlarmEvent event = new AlarmEvent(this, AlarmType.ANSWER, value.getAnswerer(), value.getQuestioner(),
                value.getAnswerer().getUserId().toString());
        publisher.publishEvent(event);

        return value;
    }

    /**
     * 질문 저장 (비로그인 사용자)
     * @param userId 피드 주인 Id
     * @param question 질문 내용
     * @param key 이미지 파일
     */
    @Override
    @Transactional
    public Question saveQuestion(Long userId, String question, String key, HttpServletRequest request) {
        Question newQuestion = new Question();
        newQuestion.setQuestion(question);  //질문 저장
        newQuestion.setQuestionDate(LocalDateTime.now()); //질문 생성일
        newQuestion.setQuestioner(null);
        newQuestion.setUserIp(getRemoteIP(request));
        newQuestion.setQuestionImageUrl(key);
        User questionPageUser = userService.findByUserId(userId);
        newQuestion.setAnswerer(questionPageUser);
        newQuestion.setQuestionDeleteYN(false);
        newQuestion.setQuestionStatus(false);
        newQuestion.setQuestionPinStatus(false);
        newQuestion.setQuestionRejected(false);
        var value = questionRepository.save(newQuestion);

        // 트랜잭션 종료 후 이벤트 발생
        AlarmEvent event = new AlarmEvent(this, AlarmType.NEW_QUESTION, value.getQuestioner(), value.getAnswerer(),
                value.getAnswerer().getUserId().toString());
        publisher.publishEvent(event);

        return value;
    }
}
