package like.lion.way.board.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardPostCommentCountResponse {

    private Long comments;

    @Builder
    public BoardPostCommentCountResponse(Long comments) {

        this.comments = comments;

    }

}
