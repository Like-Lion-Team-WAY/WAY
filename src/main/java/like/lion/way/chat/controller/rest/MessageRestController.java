package like.lion.way.chat.controller.rest;

import static like.lion.way.chat.constant.ApiMessage.NO_HAVE_MESSAGE_PERMISSION;
import static like.lion.way.chat.constant.OpenNicknameState.NICKNAME_OPEN_STATE;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import like.lion.way.ApiResponse;
import like.lion.way.chat.domain.Chat;
import like.lion.way.chat.domain.Message;
import like.lion.way.chat.domain.dto.OldMessageDTO;
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
    public ApiResponse<?> getMessages(@PathVariable("chatId") Long chatId,
                                      @RequestParam(name = "page", defaultValue = "1") int page,
                                      @RequestParam(name = "size", defaultValue = "50") int size,
                                      @RequestParam(name = "lastLoadMessageId") String lastLoadMessageId,
                                      HttpServletRequest request) {

        Long userId = getUserId(request);
        Chat chat = chatService.findById(chatId);

        if (!chat.isAccessibleUser(userId)) {
            return ApiResponse.statusAndMessage(HttpStatus.FORBIDDEN, NO_HAVE_MESSAGE_PERMISSION.get());
        }

        Page<Message> messages = findMessages(page, size, chatId, lastLoadMessageId);
        OldMessageDTO oldMessageDTO = new OldMessageDTO(MessagePageToDTOList(messages, chat), messages.isLast());

        return ApiResponse.ok(oldMessageDTO);
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
        String questionerNickname = chat.getQuestionerNickname(chat.getNicknameOpen() != NICKNAME_OPEN_STATE.get());

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
