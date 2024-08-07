package like.lion.way.chat.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "messages")
@Getter
@Setter
public class Message {
    @Id
    private String id;
    private Long chatId;
    private Long senderId;
    private Long receiverId;
    private String text;
    private String type;
    private LocalDateTime createdAt;
}