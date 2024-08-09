package like.lion.way.feed.service;

import java.util.List;
import like.lion.way.feed.domain.QuestionBox;
import like.lion.way.user.domain.User;
import org.springframework.stereotype.Service;

@Service
public interface QuestionBoxService {
    QuestionBox archieveQuestion(Long questionId, Long userId);

    List<QuestionBox> getQuestionBoxByUserId(User user);
}
