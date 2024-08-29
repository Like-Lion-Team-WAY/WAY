package like.lion.way.user.dto;

import java.util.Set;
import like.lion.way.user.domain.Interest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDto {
    private String username;
    private String nickname;
    private String userImage;
    private Set<Interest> interests;
}
