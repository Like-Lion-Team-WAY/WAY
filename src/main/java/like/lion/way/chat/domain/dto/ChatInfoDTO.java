package like.lion.way.chat.domain.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatInfoDTO {
    private Long id;
    private String name;
    private Long senderId;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private Boolean isRead;

    public ChatInfoDTO(Long id, String name, String lastMessage, Long senderId, LocalDateTime lastMessageTime, Boolean isRead) {
        this.id = id;
        this.name = name;
        this.senderId = senderId;
        this.lastMessageTime = lastMessageTime;
        this.isRead = isRead;

        if (lastMessage != null) {
            this.lastMessage = lastMessage.length() > 50 ? lastMessage.substring(0, 50) + "..." : lastMessage;
        } else {
            this.lastMessage = "아직 메세지가 없습니다";
        }
    }
}