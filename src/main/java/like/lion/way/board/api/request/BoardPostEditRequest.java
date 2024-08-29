package like.lion.way.board.api.request;

import jakarta.validation.constraints.NotNull;
import like.lion.way.board.application.request.BoardPostEditServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardPostEditRequest {

    @NotNull(message = "게시글 제목은 필수입니다.")
    private String title;

    @NotNull(message = "게시글 내용은 필수입니다.")
    private String content;

    @Builder
    public BoardPostEditRequest(String title, String content) {

        this.title = title;
        this.content = content;

    }

    public BoardPostEditServiceRequest toServiceRequest() {

        return BoardPostEditServiceRequest.builder()
                .title(title)
                .content(content)
                .build();

    }

}
