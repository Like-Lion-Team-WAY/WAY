package like.lion.way.chat.domain.dto;

import java.time.format.DateTimeFormatter;
import like.lion.way.chat.domain.Message;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReceiveMessageDTO {
    private String id;
    private String text;
    private String sendTime;
    private String type;
    private Long chatId;
    private String chatName;
    private Long senderId;
    private Long receiverId;
    private String userNickname;

    public ReceiveMessageDTO(Message message, String chatName, String userNickname) {
        this.id = message.getId();
        this.text = message.getText();
        this.type = message.getType();
        this.chatId = message.getChatId();
        this.chatName = chatName;
        this.senderId = message.getSenderId();
        this.receiverId = message.getReceiverId();
        this.userNickname = userNickname;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm");
        this.sendTime = message.getCreatedAt().format(formatter);
    }
}
