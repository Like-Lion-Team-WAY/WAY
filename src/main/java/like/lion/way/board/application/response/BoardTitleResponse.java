package like.lion.way.board.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardTitleResponse {

    private String name;

    @Builder
    public BoardTitleResponse(String name) {

        this.name = name;

    }

}
