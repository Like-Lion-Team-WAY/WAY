package like.lion.way.board.api.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import like.lion.way.board.application.request.BoardEditServiceRequest;
import like.lion.way.board.domain.AnonymousPermission;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardEditRequest {

    @NotBlank(message = "게시판 이름은 공백이면 안됩니다.")
    private String name;

    @NotBlank(message = "게시판 설명은 공백이면 안됩니다.")
    private String introduction;

    @NotNull(message = "익명 허용 여부는 필수입니다.")
    private AnonymousPermission anonymousPermission;


    @Builder
    public BoardEditRequest(
            String name,
            String introduction,
            AnonymousPermission anonymousPermission) {

        this.name = name;
        this.introduction = introduction;
        this.anonymousPermission = anonymousPermission;

    }

    public BoardEditServiceRequest toServiceRequest() {

        return BoardEditServiceRequest.builder()
                .name(name)
                .introduction(introduction)
                .AnonymousPermission(anonymousPermission)
                .build();

    }

}
