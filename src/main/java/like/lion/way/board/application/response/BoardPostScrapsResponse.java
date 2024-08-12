package like.lion.way.board.application.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardPostScrapsResponse {

    private String title;
    private String author;
    private LocalDateTime createdAt;
    private Long boardId;
    private Long boardPostId;

    @Builder
    public BoardPostScrapsResponse(
            String title,
            String author,
            LocalDateTime createdAt,
            Long boardId,
            Long boardPostId) {

        this.title = title;
        this.author = author;
        this.createdAt = createdAt;
        this.boardId = boardId;
        this.boardPostId = boardPostId;

    }

}
