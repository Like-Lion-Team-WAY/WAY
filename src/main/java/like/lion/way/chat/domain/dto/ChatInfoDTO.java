package like.lion.way.chat.domain.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatInfoDTO {
    private Long id;
    private String name;
    private String lastMessage;
    private String lastMessageTime;

    public ChatInfoDTO(Long id, String name, String lastMessage, LocalDateTime lastMessageTime) {
        this.id = id;
        this.name = name;

        if (lastMessage != null) {
            this.lastMessage = lastMessage.length() > 50 ? lastMessage.substring(0, 50) + "..." : lastMessage;
        } else {
            this.lastMessage = "아직 메세지가 없습니다";
        }

        if (lastMessageTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm");
            this.lastMessageTime = lastMessageTime.format(formatter);
        } else {
            this.lastMessageTime = "";
        }
    }
}