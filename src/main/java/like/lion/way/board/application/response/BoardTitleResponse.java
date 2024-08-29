package like.lion.way.board.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardTitleResponse {

    private Long boardId;
    private String name;
    private String introduction;
    private boolean userOwnerMatch;

    @Builder
    public BoardTitleResponse(Long boardId, String name,String introduction, boolean userOwnerMatch) {

        this.boardId = boardId;
        this.name = name;
        this.introduction = introduction;
        this.userOwnerMatch = userOwnerMatch;

    }

}
