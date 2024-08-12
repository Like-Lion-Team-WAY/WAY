package like.lion.way.chat.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomViewDTO {
    private Long userId;
    private String chatName;
    private boolean isActive;
    private boolean isQuestioner;
    private int isNicknameOpen;
}
