package like.lion.way.board.application.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardSearchServiceRequest {

    private String keyword;
    private Long boardId;

    @Builder
    public BoardSearchServiceRequest(String keyword, Long boardId) {

        this.keyword = keyword;
        this.boardId = boardId;

    }

}
