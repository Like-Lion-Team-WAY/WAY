package like.lion.way.chat.controller;

import java.util.List;
import like.lion.way.chat.domain.Message;
import like.lion.way.chat.service.MessageService;
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
    private final MessageService messageService;

    @GetMapping
    public String chatList(Model model) {
        return "pages/chat/chats";
    }

    @GetMapping("/{chatId}")
    public String chatRoom(@PathVariable Long chatId, Model model) {
        List<Message> messages = messageService.findAllMessageByChatId(chatId);
        model.addAttribute("messages", messages);
        return "pages/chat/chat-room";
    }
}
