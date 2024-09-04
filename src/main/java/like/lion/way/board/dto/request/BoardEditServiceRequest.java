package like.lion.way.board.dto.request;

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

}
