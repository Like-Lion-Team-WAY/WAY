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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<?> getChatList(HttpServletRequest request) {

        Long userId = getUserId(request);
        User user = userService.findByUserId(userId);

        List<Chat> chats = chatService.findUserChatList(user);

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

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createChat(@RequestParam(name = "questionId") Long questionId,
                                        HttpServletRequest request) {

        Long userId = getUserId(request);

        Question question = questionService.getQuestionById(questionId);

        if (question == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 질문을 찾을 수 없습니다.");
        }

        if (!question.getAnswerer().getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("채팅방 생성 권한이 없습니다");
        }

        Chat chat = chatService.findByQuestion(question);

        Map<String, Object> response = new HashMap<>();
        if (chat != null && chat.isUserActive1()) {
            response.put("message", "exist");
            response.put("chatId", chat.getId());
            return ResponseEntity.ok(response);
        }

        Chat newChat = chatService.createChat(question);
        messageService.createStartMessage(newChat);
        response.put("message", "create");
        response.put("chatId", newChat.getId());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/leave/{chatId}")
    public ResponseEntity<?> leaveChat(@PathVariable("chatId") Long chatId, HttpServletRequest request) {
        Long userId = getUserId(request);

        Chat chat = chatService.findById(chatId);

        if (chat == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("채팅방을 찾을 수 없습니다.");
        }

        if (!chat.isAccessibleUser(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 채팅방에 대한 권한이 없습니다.");
        }

        String result = chatService.userLeave(chat, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("result", result);

        String nickname = null;
        if (chat.isUser1(userId)) {
            nickname = chat.getUser1().getNickname();
        } else {
            nickname = chat.getUser2().getNickname(!chat.isNicknameOpen2());
        }
        response.put("text", "[" + nickname + "] 님이 나가셨습니다");

        return ResponseEntity.ok(response);
    }

    private Long getUserId(HttpServletRequest request) {
        String token = jwtUtil.getCookieValue(request, "accessToken");
        return jwtUtil.getUserIdFromToken(token);
    }
}
