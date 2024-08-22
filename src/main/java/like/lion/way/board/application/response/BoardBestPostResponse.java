package like.lion.way.board.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardBestPostResponse {

    private Long boardId;
    private Long postId;
    private Long likes;
    private String boardTitle;

    @Builder
    public BoardBestPostResponse(Long boardId, Long postId, Long likes, String boardTitle) {

        this.boardId = boardId;
        this.postId = postId;
        this.likes = likes;
        this.boardTitle = boardTitle;

    }

}
