package like.lion.way.chat.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OldMessageDTO {
    private List<ReceiveMessageDTO> messages;
    private boolean lastPage;
}
