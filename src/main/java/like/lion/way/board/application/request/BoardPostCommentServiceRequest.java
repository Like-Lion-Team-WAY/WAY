package like.lion.way.board.application.request;

import like.lion.way.board.domain.BoardPost;
import like.lion.way.board.domain.BoardPostComment;
import like.lion.way.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardPostCommentServiceRequest {

    private String content;
    private Long preCommentId;
    private boolean anonymousPermission;

    @Builder
    public BoardPostCommentServiceRequest(String content, Long preCommentId, boolean anonymousPermission) {

        this.content = content;
        this.preCommentId = preCommentId;
        this.anonymousPermission = anonymousPermission;

    }

    public BoardPostComment toEntity(BoardPost boardPost, User user) {

        return BoardPostComment.builder()
                .boardPost(boardPost)
                .content(content)
                .preCommentId(preCommentId)
                .anonymousPermission(anonymousPermission)
                .user(user)
                .build();

    }

}
