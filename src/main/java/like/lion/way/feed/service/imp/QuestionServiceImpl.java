package like.lion.way.feed.service.imp;

import static like.lion.way.feed.controller.GetUserIp.getRemoteIP;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import like.lion.way.feed.domain.Question;
import like.lion.way.feed.repository.QuestionRepository;
import like.lion.way.feed.service.QuestionService;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final UserService userService;

    @Value("${image.upload.dir}")
    private String uploadDir;

    @Override
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public Question getQuestionById(Long questionId) {
        return questionRepository.getByQuestionId(questionId);
    }

    @Override
    public Question updateAnswer(Long questionId, String answer) {
        Question question = questionRepository.getByQuestionId(questionId);
        question.setAnswer(answer);
        question.setAnswerDate(LocalDateTime.now());
        return questionRepository.save(question);
    }

    @Override
    public List<Question> getQuestionByAnswerer(User user) {
        return questionRepository.findQuestionsByAnswerer(user);
    }

    @Override
    public List<Question> getQuestionByQuestioner(User user) {
        return questionRepository.findQuestionsByQuestioner(user);
    }

    @Override
    public Question pinQuestion(Long questionId) {
        Question question = questionRepository.getByQuestionId(questionId);
        if(question.getQuestionPinStatus() ==true){
            question.setQuestionPinStatus(false);
        }else{
            question.setQuestionPinStatus(true);
        }
        return questionRepository.save(question);
    }

    @Override
    public Question rejectedQuestion(Question question) {
        if(question.getQuestionRejected() == true) {
            question.setQuestionRejected(false);
        }else{
            question.setQuestionRejected(true);
        }
        return questionRepository.save(question);
    }

    @Override
    public void deleteQuestion(Long questionId) {
        Question question = questionRepository.getByQuestionId(questionId);
        questionRepository.delete(question);
    }

    @Override
    @Transactional
    public Question saveQuestion(User user, Long userId, String question, boolean isAnonymous, MultipartFile image, HttpServletRequest request) {
        Question newQuestion = new Question();
        newQuestion.setQuestion(question);  //질문 저장
        newQuestion.setQuestionDate(LocalDateTime.now()); //질문 생성일
        //익명 여부에 따라
        if(isAnonymous) {
            newQuestion.setQuestioner(null);
            newQuestion.setUserIp(getRemoteIP(request));

        } else {
            newQuestion.setQuestioner(user);
        }
        if (!image.isEmpty()) { //이미지
            try {
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                String filePath = uploadDir + File.separator + fileName;
                File dest = new File(filePath);
                image.transferTo(dest);
                newQuestion.setQuestionImageUrl(fileName); // 웹에서 접근할 경로
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        User questionPageUser= userService.findByUserId(userId);
        newQuestion.setAnswerer(questionPageUser);
        newQuestion.setQuestionDeleteYN(false);
        newQuestion.setQuestionStatus(false);
        newQuestion.setQuestionPinStatus(false);
        newQuestion.setQuestionRejected(false);
        return questionRepository.save(newQuestion);
    }

    @Override
    @Transactional
    public Question saveQuestion(Question question, String answer) {
        question.setAnswer(answer);
        question.setQuestionStatus(true);
        question.setAnswerDate(LocalDateTime.now());
        return questionRepository.save(question);
    }
}
