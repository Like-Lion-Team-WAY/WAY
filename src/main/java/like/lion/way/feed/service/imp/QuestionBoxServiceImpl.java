package like.lion.way.feed.service.imp;

import java.util.List;
import like.lion.way.feed.domain.PostBox;
import like.lion.way.feed.domain.Question;
import like.lion.way.feed.domain.QuestionBox;
import like.lion.way.feed.repository.QuestionBoxRepository;
import like.lion.way.feed.service.QuestionBoxService;
import like.lion.way.feed.service.QuestionService;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionBoxServiceImpl implements QuestionBoxService {
    private final QuestionService questionService;
    private final UserService userService;
    private final QuestionBoxRepository questionBoxRepository;

    @Override
    public QuestionBox archieveQuestion(Long questionId, Long userId) {
        Question question= questionService.getQuestionById(questionId);
        User user = userService.findByUserId(userId);
        QuestionBox existingQuestionBox = questionBoxRepository.findByUserAndQuestion(user, question);

        if(existingQuestionBox != null) {
            questionBoxRepository.delete(existingQuestionBox);
            return null;
        } else {
            QuestionBox questionBox = new QuestionBox();
            questionBox.setQuestion(question);
            questionBox.setUser(user);
            return questionBoxRepository.save(questionBox);
        }
    }

    @Override
    public List<QuestionBox> getQuestionBoxByUserId(User user) {
        return questionBoxRepository.findByUser(user);
    }
}
