package like.lion.way.board.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import like.lion.way.board.application.request.BoardPostCommentServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardPostCommentRequest {

    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;

    @NotNull(message = "댓글 순서 번호는 필수입니다.")
    private Long preCommentId;

    @Builder
    public BoardPostCommentRequest(String content, Long preCommentId) {

        this.content = content;
        this.preCommentId = preCommentId;

    }

    public BoardPostCommentServiceRequest toServiceRequest() {

        return BoardPostCommentServiceRequest.builder()
                .content(content)
                .preCommentId(preCommentId)
                .build();

    }

}
