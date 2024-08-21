package like.lion.way.board.application.request;

import like.lion.way.board.domain.BoardPost;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardPostEditServiceRequest {

    private String title;
    private String content;

    @Builder
    public BoardPostEditServiceRequest(String title, String content) {

        this.title = title;
        this.content = content;

    }

}
