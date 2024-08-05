package like.lion.way.feed.controller;

import java.util.Map;
import like.lion.way.feed.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class QuestionRestcontroller {
    private final QuestionService questionService;

    @PatchMapping("/questions/answer/edit/{questionId}")
    public ResponseEntity<String> editAnswer(@PathVariable Long questionId, @RequestParam(name = "answer") String response) {
        try {
            log.info("questionId: {}", questionId);
            log.info("content: {}", response);
            questionService.updateAnswer(questionId,response);
            return ResponseEntity.ok("Answer updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update answer.");
        }
    }
}
