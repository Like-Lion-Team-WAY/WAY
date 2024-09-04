package like.lion.way.feed.service.imp;

import java.util.List;
import like.lion.way.feed.domain.Question;
import like.lion.way.feed.domain.QuestionBox;
import like.lion.way.feed.repository.QuestionBoxRepository;
import like.lion.way.feed.service.QuestionBoxService;
import like.lion.way.feed.service.QuestionService;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionBoxServiceImpl implements QuestionBoxService {
    private final QuestionService questionService;
    private final UserService userService;
    private final QuestionBoxRepository questionBoxRepository;

    /**
     * 질문 보관
     * @param questionId 질문 Id
     * @param userId 질문 보관을 한 사용자 Id
     */
    @Override
    @Transactional
    public QuestionBox archieveQuestion(Long questionId, Long userId) {
        Question question = questionService.getQuestionById(questionId);
        User user = userService.findByUserId(userId);
        QuestionBox existingQuestionBox = questionBoxRepository.findByUserAndQuestion(user, question);

        if (existingQuestionBox != null) {
            questionBoxRepository.delete(existingQuestionBox);
            return null;
        } else {
            QuestionBox questionBox = new QuestionBox();
            questionBox.setQuestion(question);
            questionBox.setUser(user);
            return questionBoxRepository.save(questionBox);
        }
    }

    /**
     * 질문 보관함 조회 (사용자로)
     * @param user 사용자
     */
    @Override
    public List<QuestionBox> getQuestionBoxByUserId(User user) {
        return questionBoxRepository.findByUser(user);
    }
}
