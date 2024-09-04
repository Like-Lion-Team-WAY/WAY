package like.lion.way.board.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardPostLikeCountResponse {

    private Long likes;

    @Builder
    public BoardPostLikeCountResponse(Long likes) {
        this.likes = likes;
    }

}
