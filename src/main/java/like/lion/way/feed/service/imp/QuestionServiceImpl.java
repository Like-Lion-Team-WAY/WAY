package like.lion.way.feed.service.imp;

import java.time.LocalDateTime;
import java.util.List;
import like.lion.way.feed.domain.Question;
import like.lion.way.feed.repository.QuestionRepository;
import like.lion.way.feed.service.QuestionService;
import like.lion.way.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Override
    public Question saveQuestion(Question newQuestion) {
        return questionRepository.save(newQuestion);
    }

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
}
