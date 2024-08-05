package like.lion.way.board.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import like.lion.way.board.application.request.BoardPostCreateServiceRequest;
import like.lion.way.board.domain.Board;
import like.lion.way.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardPostCreateRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    @NotNull(message = "익명 허용 여부는 필수입니다.")
    private boolean anonymousPermission;

    @Builder
    public BoardPostCreateRequest(String title, String content, boolean anonymousPermission) {

        this.title = title;
        this.content = content;
        this.anonymousPermission = anonymousPermission;

    }

    public BoardPostCreateServiceRequest toServiceRequest() {

        return BoardPostCreateServiceRequest.builder()
                .title(title)
                .content(content)
                .anonymousPermission(anonymousPermission)
                .build();

    }

}
