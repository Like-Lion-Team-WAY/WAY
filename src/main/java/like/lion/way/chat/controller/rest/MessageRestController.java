package like.lion.way.chat.controller.rest;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import like.lion.way.chat.domain.Chat;
import like.lion.way.chat.domain.Message;
import like.lion.way.chat.domain.dto.ReceiveMessageDTO;
import like.lion.way.chat.service.ChatService;
import like.lion.way.chat.service.MessageService;
import like.lion.way.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageRestController {
    private final ChatService chatService;
    private final MessageService messageService;
    private final JwtUtil jwtUtil;

    @GetMapping("/api/messages/{chatId}")
    public ResponseEntity<?> getMessages(@PathVariable("chatId") Long chatId,
                                         @RequestParam(name = "page", defaultValue = "1") int page,
                                         @RequestParam(name = "size", defaultValue = "50") int size,
                                         @RequestParam(name = "lastLoadMessageId") String lastLoadMessageId,
                                         HttpServletRequest request) {

        Long userId = getUserId(request);
        Chat chat = chatService.findById(chatId);

        if (!chat.isAccessibleUser(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 메세지에 대한 접근 권한이 없습니다");
        }

        Page<Message> messages = findMessages(page, size, chatId, lastLoadMessageId);

        List<ReceiveMessageDTO> receiveMessageDTOs = MessagePageToDTOList(messages, chat);

        Map<String, Object> response = new HashMap<>();
        response.put("messages", receiveMessageDTOs);
        response.put("lastPage", messages.isLast());

        return ResponseEntity.ok(response);
    }

    private Long getUserId(HttpServletRequest request) {
        String token = jwtUtil.getCookieValue(request, "accessToken");
        return jwtUtil.getUserIdFromToken(token);
    }

    private Page<Message> findMessages(int page, int size, Long chatId, String lastLoadMessageId) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());

        if (!lastLoadMessageId.isEmpty()) {
            return messageService.findAllByChatIdAndIdLessThan(chatId, lastLoadMessageId, pageable);
        } else {
            return messageService.findAllByChatId(chatId, pageable);
        }

    }

    private List<ReceiveMessageDTO> MessagePageToDTOList(Page<Message> messages, Chat chat) {
        String answererNickname = chat.getAnswererNickname();
        String questionerNickname = chat.getQuestionerNickname(chat.getNicknameOpen() != 2);

        List<ReceiveMessageDTO> receiveMessageDTOs = new ArrayList<>();
        for (Message message : messages) {
            if (chat.isAnswerer(message.getSenderId())) {
                receiveMessageDTOs.add(new ReceiveMessageDTO(message, chat.getName(), answererNickname));
            } else {
                receiveMessageDTOs.add(new ReceiveMessageDTO(message, chat.getName(), questionerNickname));
            }
        }
        return receiveMessageDTOs;
    }
}
