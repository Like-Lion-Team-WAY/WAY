package like.lion.way.chat.controller;

import jakarta.servlet.http.HttpServletRequest;
import like.lion.way.chat.domain.Chat;
import like.lion.way.chat.domain.dto.ChatRoomViewDTO;
import like.lion.way.chat.service.ChatService;
import like.lion.way.chat.service.MessageService;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final ChatService chatService;
    private final MessageService messageService;

    @GetMapping
    public String chatList(Model model, HttpServletRequest request) {
        Long userId = getUserId(request);
        String nickname = getUserNickname(userId);

        model.addAttribute("nickname", nickname);
        model.addAttribute("userId", userId);

        return "pages/chat/chats";
    }

    @GetMapping("/{chatId}")
    public String chatRoom(@PathVariable("chatId") Long chatId, Model model, HttpServletRequest request) {

        Chat chat = chatService.findById(chatId);
        if (chat == null) {
            return "error";
        }

        Long userId = getUserId(request);
        if (!chat.isAccessibleUser(userId)) {
            return "error";
        }

        messageService.readMessage(userId, chat.getId());

        ChatRoomViewDTO chatRoomViewDTO = new ChatRoomViewDTO(userId, chat.getName(), chat.isActive(),
                chat.isQuestioner(userId), chat.getNicknameOpen());

        model.addAttribute("chatRoomViewDTO", chatRoomViewDTO);

        return "pages/chat/chat-room";
    }

    private Long getUserId(HttpServletRequest request) {
        String token = jwtUtil.getCookieValue(request, "accessToken");
        return jwtUtil.getUserIdFromToken(token);
    }

    private String getUserNickname(Long userId) {
        return userService.findByUserId(userId).getNickname();
    }
}
