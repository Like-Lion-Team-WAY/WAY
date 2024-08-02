package like.lion.way.chat.repository;

import java.util.List;
import like.lion.way.chat.domain.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findAllByChatId(Long chatId);
}
