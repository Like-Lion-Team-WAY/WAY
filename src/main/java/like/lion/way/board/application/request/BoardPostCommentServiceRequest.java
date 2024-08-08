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

    @Builder
    public BoardPostCommentServiceRequest(String content, Long preCommentId) {

        this.content = content;
        this.preCommentId = preCommentId;

    }

    public BoardPostComment toEntity(BoardPost boardPost, User user) {

        return BoardPostComment.builder()
               .boardPost(boardPost)
               .content(content)
               .preCommentId(preCommentId)
               .user(user)
               .build();

    }

}
