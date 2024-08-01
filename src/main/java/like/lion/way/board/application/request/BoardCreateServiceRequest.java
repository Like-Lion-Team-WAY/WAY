package like.lion.way.board.application.request;

import like.lion.way.board.domain.Board;
import like.lion.way.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardCreateServiceRequest {

    private String name;
    private String introduction;
    private boolean anonymousPermission;
    private Long userId;

    @Builder
    public BoardCreateServiceRequest(
            String name,
            String introduction,
            boolean anonymousPermission,
            Long userId) {

        this.name = name;
        this.introduction = introduction;
        this.anonymousPermission = anonymousPermission;
        this.userId = userId;

    }

    public Board toEntity(User user) {

        return Board.builder()
                .name(name)
                .introduction(introduction)
                .anonymousPermission(anonymousPermission)
                .user(user)
                .build();

    }

}
