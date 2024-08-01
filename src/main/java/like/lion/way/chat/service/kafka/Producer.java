package like.lion.way.chat.service.kafka;

import like.lion.way.chat.domain.Message;

public interface Producer {
    public void sendMessage(Message message);
}