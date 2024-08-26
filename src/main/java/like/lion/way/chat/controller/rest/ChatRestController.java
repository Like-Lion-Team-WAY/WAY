package like.lion.way.chat.controller.rest;

import static like.lion.way.chat.constant.ApiMessage.*;
import static like.lion.way.chat.constant.ChatMessageType.*;
import static like.lion.way.chat.constant.OpenNicknameState.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import like.lion.way.ApiResponse;
import like.lion.way.chat.domain.Chat;
import like.lion.way.chat.domain.Message;
import like.lion.way.chat.domain.dto.ChatCreateResultDTO;
import like.lion.way.chat.domain.dto.ChatFuncResultDTO;
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

    /////// 채팅 리스트 페이지에 띄울 채팅 목록 보내기

    @GetMapping
    public ApiResponse<List<ChatInfoDTO>> getChatList(HttpServletRequest request) {

        Long userId = getUserId(request);
        User user = userService.findByUserId(userId);

        List<Chat> chats = chatService.findUserChatList(user);
        List<ChatInfoDTO> chatInfoDTOs = chatListToDTOList(chats);

        chatInfoDTOs.sort(
                Comparator.comparing(ChatInfoDTO::getLastMessageTime, Comparator.nullsLast(Comparator.reverseOrder())));

        return ApiResponse.ok(chatInfoDTOs);
    }

    private List<ChatInfoDTO> chatListToDTOList(List<Chat> chats) {
        List<ChatInfoDTO> chatInfoDTOs = new ArrayList<>();
        for (Chat chat : chats) {
            Message message = messageService.findLastByChatId(chat.getId());
            chatInfoDTOs.add(new ChatInfoDTO(chat, message));
        }
        return chatInfoDTOs;
    }

    //////// 새로운 채팅 생성하기

    @PostMapping
    public ApiResponse<?> createChat(@RequestParam(name = "questionId") Long questionId,
                                     HttpServletRequest request) {

        Long userId = getUserId(request);
        Question question = questionService.getQuestionById(questionId);

        ApiResponse<?> validationResponse = validateQuestionAndUser(question, userId);
        if (validationResponse.getStatus() != HttpStatus.OK) {
            return validationResponse;
        }

        return chatCreateProcessing(question);
    }

    private ApiResponse<?> chatCreateProcessing(Question question) {
        Chat chat = chatService.findByQuestion(question);

        if (chat != null && chat.isAnswererActive()) {
            ChatCreateResultDTO chatCreateResultDTO = new ChatCreateResultDTO(EXIST.get(), chat.getId());
            return ApiResponse.ok(chatCreateResultDTO);
        }

        Chat newChat = chatService.createChat(question);
        messageService.createStartMessage(newChat);
        ChatCreateResultDTO chatCreateResultDTO = new ChatCreateResultDTO(CREATE.get(), newChat.getId());
        return ApiResponse.ok(chatCreateResultDTO);
    }

    ////// 채팅방 떠나기

    @PatchMapping("/leave/{chatId}")
    public ApiResponse<?> leaveChat(@PathVariable("chatId") Long chatId, HttpServletRequest request) {
        Long userId = getUserId(request);
        Chat chat = chatService.findById(chatId);

        ApiResponse<?> validationResponse = validateChatAndUser(chat, userId);
        if (validationResponse.getStatus() != HttpStatus.OK) {
            return validationResponse;
        }

        String result = chatService.userLeave(chat, userId);
        String nickname = getNickname(chat, userId);
        ChatFuncResultDTO chatFuncResultDTO = new ChatFuncResultDTO(result, "[" + nickname + "] 님이 나가셨습니다");

        return ApiResponse.ok(chatFuncResultDTO);
    }

    /////// 채팅방 이름 변경

    @PatchMapping("name/{chatId}")
    public ApiResponse<?> updateChatName(@PathVariable("chatId") Long chatId,
                                         @RequestParam("newName") String newName,
                                         HttpServletRequest request) {
        Long userId = getUserId(request);
        Chat chat = chatService.findById(chatId);

        ApiResponse<?> validationResponse = validateChatAndUser(chat, userId);
        if (validationResponse.getStatus() != HttpStatus.OK) {
            return validationResponse;
        }

        return updateChatNameProcessing(chat, userId, newName);
    }

    private ApiResponse<?> updateChatNameProcessing(Chat chat, Long userId, String newName) {
        String oldName = chat.getName();
        if (oldName.equals(newName)) {
            ChatFuncResultDTO chatFuncResultDTO = new ChatFuncResultDTO(NO_CHANGE.get(), null);
            return ApiResponse.ok(chatFuncResultDTO);
        }

        chatService.changeName(chat, newName);

        String nickname = getNickname(chat, userId);
        String text = "[" + nickname + "] 님이 채팅방 이름을 변경하였습니다<br>"
                + "[" + oldName + "] => [" + newName + "]";
        ChatFuncResultDTO chatFuncResultDTO = new ChatFuncResultDTO(CHANGE.get(), text);

        return ApiResponse.ok(chatFuncResultDTO);
    }

    ////// 닉네임 요청 및 취소

    @PatchMapping("/nickname-request/{chatId}")
    public ApiResponse<?> nicknameRequest(@PathVariable("chatId") Long chatId, @RequestParam("type") String type,
                                          HttpServletRequest request) {

        Long userId = getUserId(request);
        Chat chat = chatService.findById(chatId);

        ApiResponse<?> validationResponse = validateChatAndNicknameRequest(chat, userId);
        if (validationResponse.getStatus() != HttpStatus.OK) {
            return validationResponse;
        }

        return nicknameRequestProcessing(type, chat, userId);
    }

    private ApiResponse<?> nicknameRequestProcessing(String type, Chat chat, Long userId) {
        if (type.equals(REQUEST.get())) {
            return requestTypeProcessing(type, chat, userId);
        } else if (type.equals(CANCEL.get())) {
            return cancelTypeProcessing(type, chat, userId);
        } else {
            return ApiResponse.statusAndMessage(HttpStatus.BAD_REQUEST, NOT_RIGHT_TYPE.get());
        }
    }

    private ApiResponse<?> requestTypeProcessing(String type, Chat chat, Long userId) {
        if (chat.getNicknameOpen() == NICKNAME_NO_OPEN_STATE.get()) {
            chatService.changeNicknameOpen(chat, NICKNAME_REQUEST_STATE.get());
            String nickname = getNickname(chat, userId);
            String text = "[" + nickname + "] 님이 닉네임을 요청하였습니다.";
            ChatFuncResultDTO chatFuncResultDTO = new ChatFuncResultDTO(type, text);
            return ApiResponse.ok(chatFuncResultDTO);
        } else {
            return ApiResponse.statusAndMessage(HttpStatus.CONFLICT, ALREADY_PROCESSED.get());
        }
    }

    private ApiResponse<?> cancelTypeProcessing(String type, Chat chat, Long userId) {
        if (chat.getNicknameOpen() == NICKNAME_REQUEST_STATE.get()) {
            chatService.changeNicknameOpen(chat, NICKNAME_NO_OPEN_STATE.get());
            String nickname = getNickname(chat, userId);
            String text = "[" + nickname + "] 님이 닉네임을 요청을 취소하였습니다.";
            ChatFuncResultDTO chatFuncResultDTO = new ChatFuncResultDTO(type, text);
            return ApiResponse.ok(chatFuncResultDTO);
        } else {
            return ApiResponse.statusAndMessage(HttpStatus.BAD_REQUEST, NO_NICKNAME_REQUEST_TO_CANCEL.get());
        }
    }

    ///// 닉네임 수락 및 거절

    @PatchMapping("/nickname-response/{chatId}")
    public ApiResponse<?> nicknameResponse(@PathVariable("chatId") Long chatId, @RequestParam("type") String type,
                                           HttpServletRequest request) {

        Long userId = getUserId(request);
        Chat chat = chatService.findById(chatId);

        ApiResponse<?> validationResponse = validateChatAndNicknameResponse(chat, userId);
        if (validationResponse.getStatus() != HttpStatus.OK) {
            return validationResponse;
        }

        return nicknameResponseProcessing(type, chat, userId);
    }

    private ApiResponse<?> nicknameResponseProcessing(String type, Chat chat, Long userId) {
        if (type.equals(ACCEPT.get())) {
            return acceptTypeProcessing(type, chat, userId);
        } else if (type.equals(REJECT.get())) {
            return rejectTypeProcessing(type, chat, userId);
        } else {
            return ApiResponse.statusAndMessage(HttpStatus.BAD_REQUEST, NOT_RIGHT_TYPE.get());
        }
    }

    private ApiResponse<?> acceptTypeProcessing(String type, Chat chat, Long userId) {
        chatService.changeNicknameOpen(chat, NICKNAME_OPEN_STATE.get());
        String nickname = getNickname(chat, userId);
        String text = "[" + nickname + "] 님이 닉네임 요청을 수락하셨습니다.";
        ChatFuncResultDTO chatFuncResultDTO = new ChatFuncResultDTO(type, text);
        return ApiResponse.ok(chatFuncResultDTO);
    }

    private ApiResponse<?> rejectTypeProcessing(String type, Chat chat, Long userId) {
        chatService.changeNicknameOpen(chat, NICKNAME_NO_OPEN_STATE.get());
        String nickname = getNickname(chat, userId);
        String text = "[" + nickname + "] 님이 닉네임 요청을 거절하셨습니다.";
        ChatFuncResultDTO chatFuncResultDTO = new ChatFuncResultDTO(type, text);
        return ApiResponse.ok(chatFuncResultDTO);
    }

    ////// 공통

    private Long getUserId(HttpServletRequest request) {
        String token = jwtUtil.getCookieValue(request, "accessToken");
        return jwtUtil.getUserIdFromToken(token);
    }

    private String getNickname(Chat chat, Long userId) {
        if (chat.isAnswerer(userId)) {
            return chat.getAnswererNickname();
        } else {
            return chat.getQuestionerNickname(chat.getNicknameOpen() != NICKNAME_OPEN_STATE.get());
        }
    }

    /// validation

    private ApiResponse<?> validateQuestionAndUser(Question question, Long userId) {
        if (question == null) {
            return ApiResponse.statusAndMessage(HttpStatus.NOT_FOUND, CANNOT_FIND_QUESTION.get());
        }

        if (!question.getAnswerer().getUserId().equals(userId)) {
            return ApiResponse.statusAndMessage(HttpStatus.FORBIDDEN, NO_HAVE_CREATE_CHAT_PERMISSION.get());
        }

        if (question.getQuestioner() == null) {
            return ApiResponse.statusAndMessage(HttpStatus.BAD_REQUEST, CANNOT_CHAT_WITH_NON_MEMBER.get());
        }

        return ApiResponse.ok();
    }

    private ApiResponse<?> validateChatAndUser(Chat chat, Long userId) {
        if (chat == null) {
            return ApiResponse.statusAndMessage(HttpStatus.NOT_FOUND, CANNOT_FIND_CHAT.get());
        }

        if (!chat.isAccessibleUser(userId)) {
            return ApiResponse.statusAndMessage(HttpStatus.FORBIDDEN, NO_HAVE_CHAT_PERMISSION.get());
        }

        return ApiResponse.ok();
    }

    private ApiResponse<?> validateChatAndNicknameRequest(Chat chat, Long userId) {
        if (chat == null) {
            return ApiResponse.statusAndMessage(HttpStatus.NOT_FOUND, CANNOT_FIND_CHAT.get());
        }

        if (!chat.isAnswerer(userId)) {
            return ApiResponse.statusAndMessage(HttpStatus.FORBIDDEN, NO_HAVE_REQUEST_NICKNAME_PERMISSION.get());
        }

        return ApiResponse.ok();
    }

    private ApiResponse<?> validateChatAndNicknameResponse(Chat chat, Long userId) {
        if (chat == null) {
            return ApiResponse.statusAndMessage(HttpStatus.NOT_FOUND, CANNOT_FIND_CHAT.get());
        }

        if (!chat.isQuestioner(userId)) {
            return ApiResponse.statusAndMessage(HttpStatus.FORBIDDEN, NO_HAVE_ACCEPT_NICKNAME_PERMISSION.get());
        }

        if (chat.getNicknameOpen() == NICKNAME_NO_OPEN_STATE.get()) {
            return ApiResponse.statusAndMessage(HttpStatus.BAD_REQUEST, NO_NICKNAME_REQUEST.get());
        }

        if (chat.getNicknameOpen() == NICKNAME_OPEN_STATE.get()) {
            return ApiResponse.statusAndMessage(HttpStatus.CONFLICT, ALREADY_PROCESSED.get());
        }

        return ApiResponse.ok();
    }
}
