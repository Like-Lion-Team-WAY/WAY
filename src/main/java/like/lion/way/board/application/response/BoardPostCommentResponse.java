package like.lion.way.board.application.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardPostCommentResponse {

    private String commentUsername;
    private String commentContent;
    private LocalDateTime commentCreatedAt;
    private Long commentId;
    private Long preCommentId;

    @Builder
    public BoardPostCommentResponse(String commentUsername, String commentContent, LocalDateTime commentCreatedAt, Long commentId, Long preCommentId) {

        this.commentUsername = commentUsername;
        this.commentContent = commentContent;
        this.commentCreatedAt = commentCreatedAt;
        this.commentId = commentId;
        this.preCommentId = preCommentId;

    }

}