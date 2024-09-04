package like.lion.way.board.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import like.lion.way.board.dto.request.BoardCreateServiceRequest;
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



    @Builder
    public BoardCreateRequest(
            String name,
            String introduction) {

        this.name = name;
        this.introduction = introduction;

    }

    public BoardCreateServiceRequest toServiceRequest() {

        return BoardCreateServiceRequest.builder()
                .name(name)
                .introduction(introduction)
                .build();

    }

}
