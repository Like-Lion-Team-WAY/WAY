package like.lion.way.board.application.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardPostScrapsResponse {

    private String title;
    private String nickname;
    private String username;
    private LocalDateTime createdAt;
    private Long boardId;
    private Long boardPostId;

    @Builder
    public BoardPostScrapsResponse(
            String title,
            String nickname,
            LocalDateTime createdAt,
            Long boardId,
            Long boardPostId,
            String username) {

        this.title = title;
        this.nickname = nickname;
        this.username = username;
        this.createdAt = createdAt;
        this.boardId = boardId;
        this.boardPostId = boardPostId;

    }

}
