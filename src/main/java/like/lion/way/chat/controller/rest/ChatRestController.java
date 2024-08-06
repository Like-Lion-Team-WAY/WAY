package like.lion.way.chat.controller.rest;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import like.lion.way.chat.domain.Chat;
import like.lion.way.chat.domain.Message;
import like.lion.way.chat.domain.dto.ChatInfoDTO;
import like.lion.way.chat.service.ChatService;
import like.lion.way.chat.service.MessageService;
import like.lion.way.feed.domain.Question;
import like.lion.way.feed.service.QuestionService;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatRestController {

    private final ChatService chatService;
    private final MessageService messageService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<?> getChatList(@RequestParam(name = "page", defaultValue = "1") int page,
                                         @RequestParam(name = "size", defaultValue = "10") int size,
                                         HttpServletRequest request) {

        String token = jwtUtil.getCookieValue(request, "accessToken");
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findByUserId(userId);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id"));

        Page<Chat> chats = chatService.findAllByUser1OrUser2(user, pageable);

        List<ChatInfoDTO> chatInfoDTOs = new ArrayList<>();
        for (Chat chat : chats) {
            Message message = messageService.findLastByChatId(chat.getId());

            if (message != null) {
                chatInfoDTOs.add(
                        new ChatInfoDTO(chat.getId(), chat.getName(), message.getText(), message.getCreatedAt()));
            } else {
                chatInfoDTOs.add(new ChatInfoDTO(chat.getId(), chat.getName(), "메세지가 없습니다", null));
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("chats", chatInfoDTOs);
        response.put("lastPage", chats.isLast());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createChat(@RequestParam(name = "questionId") Long questionId,
                                        HttpServletRequest request) {

        String token = jwtUtil.getCookieValue(request, "accessToken");
        Long userId = jwtUtil.getUserIdFromToken(token);

        Question question = questionService.getQuestionById(questionId);


        if (question == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 질문을 찾을 수 없습니다.");
        }

        if (!question.getAnswerer().getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("채팅 생성 권한이 없습니다");
        }

        Chat chat = chatService.findByQuestion(question);

        Map<String, Object> response = new HashMap<>();
        if (chat != null) {
            response.put("message", "이미 생성된 채팅방입니다.");
            response.put("chatId", chat.getId());
            return ResponseEntity.ok(response);
        }

        Chat newChat = chatService.createChat(question);
        messageService.createStartMessage(newChat);
        response.put("message", "채팅방을 생성했습니다.");
        response.put("chatId", newChat.getId());
        return ResponseEntity.ok(response);
    }

}
