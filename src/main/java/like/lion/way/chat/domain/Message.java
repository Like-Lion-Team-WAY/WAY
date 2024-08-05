package like.lion.way.chat.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "messages")
@Getter
@Setter
public class Message {
    @Id
    private String id;
    private Long chatId;
    private Long userId;
    private String text;
    private LocalDateTime createdAt;
}