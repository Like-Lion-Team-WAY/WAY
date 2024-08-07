package like.lion.way.board.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardTitleResponse {

    private Long boardId;
    private String name;

    @Builder
    public BoardTitleResponse(Long boardId, String name) {

        this.boardId = boardId;
        this.name = name;

    }

}
