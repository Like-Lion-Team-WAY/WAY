package like.lion.way.chat.domain.dto;

import java.time.LocalDateTime;
import like.lion.way.chat.domain.Chat;
import like.lion.way.chat.domain.Message;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ChatInfoDTO {
    private Long id;
    private String name;
    private Long senderId;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private Boolean isRead;

    public ChatInfoDTO(Chat chat, Message message) {

        if (message != null) {
            setDTO(chat.getId(), chat.getName(), message.getText(), message.getSenderId(),
                    message.getCreatedAt(), message.getIsRead());
        } else {
            setDTO(chat.getId(), chat.getName(), "메세지가 없습니다", null, null, true);
        }
    }

    private void setDTO(Long id, String name, String lastMessage, Long senderId, LocalDateTime lastMessageTime,
                        Boolean isRead) {
        this.id = id;
        this.name = name;
        this.senderId = senderId;
        this.lastMessageTime = lastMessageTime;
        this.isRead = isRead;
        this.lastMessage = lastMessage.length() > 50 ? lastMessage.substring(0, 50) + "..." : lastMessage;
    }
}