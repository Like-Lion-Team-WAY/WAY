package like.lion.way.board.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardTitleResponse {

    private Long boardPostId;
    private String name;

    @Builder
    public BoardTitleResponse(Long boardPostId, String name) {

        this.boardPostId = boardPostId;
        this.name = name;

    }

}
