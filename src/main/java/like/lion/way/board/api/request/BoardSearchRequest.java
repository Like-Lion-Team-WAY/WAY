package like.lion.way.board.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import like.lion.way.board.application.request.BoardSearchServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardSearchRequest {

    @NotBlank(message = "검색할 내용은 필수입니다.")
    private String keyword;

    @NotNull(message = "게시판 id는 필수입니다.")
    private Long boardId;

    @Builder
    public BoardSearchRequest(String keyword, Long boardId) {

        this.keyword = keyword;
        this.boardId = boardId;

    }

    public BoardSearchServiceRequest toServiceRequest() {

        return BoardSearchServiceRequest.builder()
                .keyword(keyword)
                .boardId(boardId)
                .build();

    }

}
