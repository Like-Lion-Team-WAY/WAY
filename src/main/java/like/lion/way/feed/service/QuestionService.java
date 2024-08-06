package like.lion.way.feed.service;

import java.util.List;
import like.lion.way.feed.domain.Question;
import like.lion.way.user.domain.User;
import org.springframework.stereotype.Service;

@Service
public interface QuestionService {
    Question saveQuestion(Question newQuestion);

    List<Question> getAllQuestions();

    Question getQuestionById(Long questionId);

    Question updateAnswer(Long questionId, String answer);

    List<Question> getQuestionByAnswerer(User user);

    List<Question> getQuestionByQuestioner(User user);
}
