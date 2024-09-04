package like.lion.way.feed.controller;

import like.lion.way.feed.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuestionRestcontroller {
    private final QuestionService questionService;

    /**
     * 질문에 대한 답변 수정
     * @param questionId 답변할 질문 Id
     * @param response 질문 답변 내용
     */
    @PatchMapping("/questions/answer/edit/{questionId}")
    public ResponseEntity<String> editAnswer(@PathVariable("questionId") Long questionId,
                                             @RequestParam(name = "answer") String response) {

        try {
            questionService.updateAnswer(questionId, response);
            return ResponseEntity.ok("답변 수정 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("답변 수정 실패");
        }
    }
}
