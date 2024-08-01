package like.lion.way.board.application.request;

import like.lion.way.board.domain.Board;
import like.lion.way.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardEditServiceRequest {

    private String name;
    private String introduction;
    private boolean anonymousPermission;


    @Builder
    public BoardEditServiceRequest(
            boolean anonymousPermission,
            String name,
            String introduction) {

        this.anonymousPermission = anonymousPermission;
        this.name = name;
        this.introduction = introduction;

    }

    public Board toEntity(User user) {

        return Board.builder()
                .name(name)
                .introduction(introduction)
                .user(user)
                .anonymousPermission(anonymousPermission)
                .build();
    }

}
