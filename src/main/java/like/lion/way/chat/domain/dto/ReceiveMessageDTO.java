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
    private Long chatId;
    private Long userId;
    private String userNickname;

    public ReceiveMessageDTO(Message message, String userNickname) {
        this.id = message.getId();
        this.text = message.getText();
        this.chatId = message.getChatId();
        this.userId = message.getUserId();
        this.userNickname = userNickname;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm");
        this.sendTime = message.getCreatedAt().format(formatter);
    }
}
