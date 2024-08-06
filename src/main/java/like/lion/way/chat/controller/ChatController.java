package like.lion.way.chat.controller;

import jakarta.servlet.http.HttpServletRequest;
import like.lion.way.chat.domain.Chat;
import like.lion.way.chat.service.ChatService;
import like.lion.way.jwt.util.JwtUtil;
import like.lion.way.user.domain.User;
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

    @GetMapping
    public String chatList(Model model, HttpServletRequest request) {
        String token = jwtUtil.getCookieValue(request, "accessToken");
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findByUserId(userId);

        model.addAttribute("nickname", user.getNickname());

        return "pages/chat/chats";
    }

    @GetMapping("/{chatId}")
    public String chatRoom(@PathVariable("chatId") Long chatId, Model model, HttpServletRequest request) {
        Chat chat = chatService.findById(chatId);

        if (chat == null) {
            return "error";
        }

        String token = jwtUtil.getCookieValue(request, "accessToken");
        Long userId = jwtUtil.getUserIdFromToken(token);

        if (!chat.isAccessibleUser(userId)) {
            return "error";
        }

        model.addAttribute("userId", userId);
        model.addAttribute("chatName", chat.getName());
        model.addAttribute("isActive", chat.isActive());

        return "pages/chat/chat-room";
    }
}
