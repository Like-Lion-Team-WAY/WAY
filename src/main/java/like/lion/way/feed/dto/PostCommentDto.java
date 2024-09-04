package like.lion.way.feed.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCommentDto {
    private String postCommentContent;
    private Long userId;
}
