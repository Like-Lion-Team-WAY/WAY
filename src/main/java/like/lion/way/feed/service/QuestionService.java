package like.lion.way.feed.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import like.lion.way.feed.domain.Question;
import like.lion.way.user.domain.User;



public interface QuestionService {

    List<Question> getAllQuestions();

    Question getQuestionById(Long questionId);

    Question updateAnswer(Long questionId, String answer);

    List<Question> getQuestionByAnswerer(User user);

    List<Question> getQuestionByQuestioner(User user);

    Question pinQuestion(Long questionId);

    Question rejectedQuestion(Question question);

    void deleteQuestion(Long questionId);

    Question saveQuestion(User user, Long userId, String question, boolean isAnonymous, String key, HttpServletRequest request);

    Question saveQuestion(Question question, String answer);

    Question saveQuestion(Long userId, String question, String key, HttpServletRequest request);
}
