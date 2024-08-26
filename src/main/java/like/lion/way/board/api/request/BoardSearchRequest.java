package like.lion.way.board.api.request;

import jakarta.validation.constraints.NotBlank;
import like.lion.way.board.application.request.BoardSearchServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardSearchRequest {

    @NotBlank(message = "검색할 내용은 필수입니다.")
    private String keyword;

    @Builder
    public BoardSearchRequest(String keyword) {
        this.keyword = keyword;
    }

    public BoardSearchServiceRequest toServiceRequest() {

        return BoardSearchServiceRequest.builder()
                .keyword(keyword)
                .build();

    }

}
