package like.lion.way.chat.controller.rest;

import co.elastic.clients.elasticsearch.nodes.Http;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

        chatInfoDTOs.sort(
                Comparator.comparing(ChatInfoDTO::getLastMessageTime, Comparator.nullsLast(Comparator.reverseOrder())));

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
        if (chat != null && chat.isAnswererActive()) {
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

    @PatchMapping("/leave/{chatId}")
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

        String nickname = getNickname(chat, userId);
        response.put("text", "[" + nickname + "] 님이 나가셨습니다");

        return ResponseEntity.ok(response);
    }

    @PatchMapping("name/{chatId}")
    public ResponseEntity<?> updateChatName(@PathVariable("chatId") Long chatId,
                                            @RequestParam("newName") String newName,
                                            HttpServletRequest request) {
        Long userId = getUserId(request);
        Chat chat = chatService.findById(chatId);

        if (chat == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("채팅방을 찾을 수 없습니다.");
        }

        if (!chat.isAccessibleUser(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 채팅방에 대한 권한이 없습니다.");
        }

        Map<String, Object> response = new HashMap<>();

        String oldName = chat.getName();
        if (oldName.equals(newName)) {
            response.put("result", "noChange");
            return ResponseEntity.ok(response);
        }

        chatService.changeName(chat, newName);

        response.put("result", "change");

        String nickname = getNickname(chat, userId);
        String text = "[" + nickname + "] 님이 채팅방 이름을 변경하였습니다<br>"
                + "[" + oldName + "] => [" + newName + "]";
        response.put("text", text);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/nickname-request/{chatId}")
    public ResponseEntity<?> nicknameRequest(@PathVariable("chatId") Long chatId, @RequestParam("type") String type,
                                             HttpServletRequest request) {

        Long userId = getUserId(request);

        Chat chat = chatService.findById(chatId);

        if (chat == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("채팅방을 찾을 수 없습니다.");
        }

        if (!chat.isAnswerer(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("닉네임 요청에 대한 권한이 없습니다.");
        }

        Map<String, Object> response = new HashMap<>();
        if (type.equals("request")) {
            if (chat.getNicknameOpen() == 0) {
                chatService.changeNicknameOpen(chat, 1);
                String nickname = getNickname(chat, userId);
                String text = "[" + nickname + "] 님이 닉네임을 요청하였습니다.";
                response.put("text", text);
                response.put("result", type);
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 처리된 요청입니다.");
            }
        } else if (type.equals("cancel")) {
            if (chat.getNicknameOpen() == 1) {
                chatService.changeNicknameOpen(chat, 0);
                String nickname = getNickname(chat, userId);
                String text = "[" + nickname + "] 님이 닉네임을 요청을 취소하였습니다.";
                response.put("text", text);
                response.put("result", type);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("취소할 요청이 없습니다.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("적절한 타입이 아닙니다.");
        }

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/nickname-response/{chatId}")
    public ResponseEntity<?> nicknameResponse(@PathVariable("chatId") Long chatId, @RequestParam("type") String type,
                                              HttpServletRequest request) {

        Long userId = getUserId(request);

        Chat chat = chatService.findById(chatId);

        if (chat == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("채팅방을 찾을 수 없습니다.");
        }

        if (!chat.isQuestioner(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("닉네임 수락에 대한 권한이 없습니다.");
        }

        if (chat.getNicknameOpen() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("닉네임에 대한 요청이 없습니다.");
        }

        if (chat.getNicknameOpen() == 2) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 처리된 요청입니다.");
        }

        Map<String, Object> response = new HashMap<>();
        if (type.equals("accept")) {
            chatService.changeNicknameOpen(chat, 2);
            String nickname = getNickname(chat, userId);
            String text = "[" + nickname + "] 님이 닉네임 요청을 수락하셨습니다.";
            response.put("text", text);
            response.put("result", type);
        } else if (type.equals("reject")) {
            chatService.changeNicknameOpen(chat, 0);
            String nickname = getNickname(chat, userId);
            String text = "[" + nickname + "] 님이 닉네임 요청을 거절하셨습니다.";
            response.put("text", text);
            response.put("result", type);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("적절한 타입이 아닙니다.");
        }

        return ResponseEntity.ok(response);
    }

    private Long getUserId(HttpServletRequest request) {
        String token = jwtUtil.getCookieValue(request, "accessToken");
        return jwtUtil.getUserIdFromToken(token);
    }

    private String getNickname(Chat chat, Long userId) {
        if (chat.isAnswerer(userId)) {
            return chat.getAnswerer().getNickname();
        } else {
            return chat.getQuestioner().getNickname(chat.getNicknameOpen() != 2);
        }
    }
}
