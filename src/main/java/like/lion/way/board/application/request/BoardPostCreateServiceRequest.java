package like.lion.way.board.application.request;

import like.lion.way.board.domain.Board;
import like.lion.way.board.domain.BoardPost;
import like.lion.way.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardPostCreateServiceRequest {

    private String title;
    private String content;
    private boolean anonymousPermission;

    @Builder
    public BoardPostCreateServiceRequest(String title, String content, boolean anonymousPermission) {

        this.title = title;
        this.content = content;
        this.anonymousPermission = anonymousPermission;

    }

    public BoardPost toEntity(User user, Board board) {

        System.out.println("BoardPost to Entity 포스트 제목 :::" + title);

        return BoardPost.builder()
                .user(user)
                .board(board)
                .title(title)
                .content(content)
                .anonymousPermission(anonymousPermission)
                .build();

    }

}
