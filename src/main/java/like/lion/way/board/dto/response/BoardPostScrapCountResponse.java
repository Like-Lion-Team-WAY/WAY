package like.lion.way.board.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardPostScrapCountResponse {

    private Long scraps;

    @Builder
    public BoardPostScrapCountResponse(Long scraps) {

        this.scraps = scraps;

    }

}
