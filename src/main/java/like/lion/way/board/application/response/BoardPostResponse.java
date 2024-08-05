package like.lion.way.board.application.response;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.LocalDateTime;
import lombok.Builder;

public class BoardPostResponse {

    private String boardName;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postNum;
    private String postTitle;
    private String author;
    private LocalDateTime created_at;

    @Builder
    public BoardPostResponse(
            String boardName,
            String postTitle,
            String author,
            LocalDateTime created_at) {

        this.boardName = boardName;
        this.postTitle = postTitle;
        this.author = author;
        this.created_at = created_at;

    }

}