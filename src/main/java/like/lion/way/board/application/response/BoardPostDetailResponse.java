package like.lion.way.board.application.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardPostDetailResponse {

    private String author;
    private LocalDateTime postCreatedAt;
    private String postTitle;
    private String postContent;
    private Long postLikes;
    private Long postComments;
    private Long postScraps;
    private List<BoardPostCommentResponse> boardPostCommentsList;

    @Builder
    public BoardPostDetailResponse(LocalDateTime postCreatedAt, String postTitle, String postContent, String author,
                                   Long postLikes, Long postComments, Long postScraps,
                                   List<BoardPostCommentResponse> boardPostCommentsList) {

        this.postCreatedAt = postCreatedAt;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.author = author;
        this.postLikes = postLikes;
        this.postComments = postComments;
        this.postScraps = postScraps;
        this.boardPostCommentsList = boardPostCommentsList;

    }

}
