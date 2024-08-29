package like.lion.way.chat.repository;

import java.util.List;
import like.lion.way.chat.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    Page<Message> findAllByChatId(Long chatId, Pageable pageable);
    Message findFirstByChatIdOrderByCreatedAtDesc(Long id);
    Page<Message> findAllByChatIdAndIdLessThan(Long chatId, String lastLoadMessageId, Pageable pageable);
    void deleteByChatId(Long chatId);
    List<Message> findByChatIdAndReceiverIdAndIsReadFalse(Long chatId, Long userId);
}
