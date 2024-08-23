package like.lion.way.board.application.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardSearchServiceRequest {

    private String keyword;

    @Builder
    public BoardSearchServiceRequest(String keyword) {
        this.keyword = keyword;
    }

}
