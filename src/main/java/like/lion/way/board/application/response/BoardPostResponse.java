package like.lion.way.board.application.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardPostResponse {

    private String boardName;
    private Long boardPostId;
    private String postTitle;
    private String nickname;
    private LocalDateTime created_at;

    @Builder
    public BoardPostResponse(
            String boardName,
            Long boardPostId,
            String postTitle,
            String nickname,
            LocalDateTime created_at) {

        this.boardName = boardName;
        this.boardPostId = boardPostId;
        this.postTitle = postTitle;
        this.nickname = nickname;
        this.created_at = created_at;

    }

}
