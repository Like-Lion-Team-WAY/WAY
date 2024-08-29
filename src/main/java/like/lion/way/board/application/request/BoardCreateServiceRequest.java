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

    @Builder
    public BoardCreateServiceRequest(
            String name,
            String introduction) {

        this.name = name;
        this.introduction = introduction;

    }

    public Board toEntity(User user) {

        return Board.builder()
                .name(name)
                .introduction(introduction)
                .user(user)
                .build();

    }

}
