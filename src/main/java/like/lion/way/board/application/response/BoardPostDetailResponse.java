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
    private String authorProfileImgUrl;
    private LocalDateTime postCreatedAt;
    private String postTitle;
    private String postContent;
    private Long postLikes;
    private Long postComments;
    private Long postScraps;
    private List<BoardPostCommentResponse> boardPostCommentsList;
    private boolean userOwnerMatch;

    @Builder
    public BoardPostDetailResponse(LocalDateTime postCreatedAt, String postTitle, String postContent, String author,
                                   String authorProfileImgUrl, Long postLikes, Long postComments, Long postScraps,
                                   List<BoardPostCommentResponse> boardPostCommentsList,
                                   boolean userOwnerMatch) {

        this.postCreatedAt = postCreatedAt;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.author = author;
        this.authorProfileImgUrl = authorProfileImgUrl;
        this.postLikes = postLikes;
        this.postComments = postComments;
        this.postScraps = postScraps;
        this.boardPostCommentsList = boardPostCommentsList;
        this.userOwnerMatch = userOwnerMatch;

    }

}
