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
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatService chatService;
    private final MessageService messageService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/api/chats")
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
}
