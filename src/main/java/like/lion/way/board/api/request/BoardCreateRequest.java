package like.lion.way.board.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import like.lion.way.board.application.request.BoardCreateServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardCreateRequest {

    @NotBlank(message = "게시판 이름은 필수입니다.")
    private String name;

    @NotBlank(message = "게시판 설명은 필수입니다.")
    private String introduction;

    @NotBlank(message = "익명 허용 여부는 필수입니다.")
    private boolean anonymousPermission;

    @NotNull(message = "관리 유저는 필수입니다.")
    private Long userId;

    @Builder
    public BoardCreateRequest(
            String name,
            String introduction,
            boolean anonymousPermission,
            Long userId) {

        this.name = name;
        this.introduction = introduction;
        this.anonymousPermission = anonymousPermission;
        this.userId = userId;

    }

    public BoardCreateServiceRequest toServiceRequest() {

        return BoardCreateServiceRequest.builder()
                .name(name)
                .introduction(introduction)
                .anonymousPermission(anonymousPermission)
                .userId(userId)
                .build();

    }

}
