package like.lion.way.chat.controller.rest;

import static like.lion.way.chat.constant.ApiMessage.ALREADY_PROCESSED;
import static like.lion.way.chat.constant.ApiMessage.CANNOT_CHAT_WITH_NON_MEMBER;
import static like.lion.way.chat.constant.ApiMessage.CANNOT_FIND_CHAT;
import static like.lion.way.chat.constant.ApiMessage.CANNOT_FIND_QUESTION;
import static like.lion.way.chat.constant.ApiMessage.NOT_RIGHT_TYPE;
import static like.lion.way.chat.constant.ApiMessage.NO_HAVE_ACCEPT_NICKNAME_PERMISSION;
import static like.lion.way.chat.constant.ApiMessage.NO_HAVE_CHAT_PERMISSION;
import static like.lion.way.chat.constant.ApiMessage.NO_HAVE_CREATE_CHAT_PERMISSION;
import static like.lion.way.chat.constant.ApiMessage.NO_HAVE_REQUEST_NICKNAME_PERMISSION;
import static like.lion.way.chat.constant.ApiMessage.NO_NICKNAME_REQUEST;
import static like.lion.way.chat.constant.ApiMessage.NO_NICKNAME_REQUEST_TO_CANCEL;
import static like.lion.way.chat.constant.ChatMessageType.ACCEPT;
import static like.lion.way.chat.constant.ChatMessageType.CANCEL;
import static like.lion.way.chat.constant.ChatMessageType.CHANGE;
import static like.lion.way.chat.constant.ChatMessageType.CREATE;
import static like.lion.way.chat.constant.ChatMessageType.EXIST;
import static like.lion.way.chat.constant.ChatMessageType.NO_CHANGE;
import static like.lion.way.chat.constant.ChatMessageType.REJECT;
import static like.lion.way.chat.constant.ChatMessageType.REQUEST;
import static like.lion.way.chat.constant.OpenNicknameState.NICKNAME_NO_OPEN_STATE;
import static like.lion.way.chat.constant.OpenNicknameState.NICKNAME_OPEN_STATE;
import static like.lion.way.chat.constant.OpenNicknameState.NICKNAME_REQUEST_STATE;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 채팅방에 대한 api controller
 *
 * @author : Lee NaYeon
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatRestController {

    private final ChatService chatService;
    private final MessageService messageService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final QuestionService questionService;

    /**
     * 채팅방 목록 불러오기 api
     *
     * @param request 유저 정보 추출 용도
     * @return 응답 결과 + 채팅방 목록
     */
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

    /**
     * Chat 리스트 -> ChatInfoDTO 로 변환
     *
     * @param chats 변환할 chat list
     * @return ChatInfoDTO로 변환된 리스트 데이터
     */
    private List<ChatInfoDTO> chatListToDTOList(List<Chat> chats) {
        List<ChatInfoDTO> chatInfoDTOs = new ArrayList<>();
        for (Chat chat : chats) {
            Message message = messageService.findLastByChatId(chat.getId());
            chatInfoDTOs.add(new ChatInfoDTO(chat, message));
        }
        return chatInfoDTOs;
    }

    //////////////////////////////////

    /**
     * 새로운 채팅방 생성 api
     *
     * @param questionId 채팅이 생성될 질문 id
     * @param request 유저 정보 추출 용도
     * @return 응답 결과 + 채팅 생성 결과 데이터 (ok시)
     */
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

    /**
     * 기존 방 여부에 따른 채팅방 생성
     *
     * @param question 채팅이 생성될 질문
     * @return 응답 결과 + 채팅 생성 결과 데이터
     */
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

    //////////////////////////////////

    /**
     * 채팅방 나가기 api
     *
     * @param chatId 나가기 요청한 채팅방 id
     * @param request 유저 정보 추출 용도
     * @return 응답 결과 + 채팅 기능 결과 데이터 (ok시)
     */
    @PatchMapping("/leave/{chatId}")
    public ApiResponse<?> leaveChat(@PathVariable("chatId") Long chatId,HttpServletRequest request) {
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

    //////////////////////////////////

    /**
     * 채팅방 이름 변경 api
     *
     * @param chatId 이름 변경 요청한 채팅방 id
     * @param newName 변경될 이름
     * @param request 유저 정보 추출 용도
     * @return 응답 결과 + 채팅 기능 결과 데이터 (ok시)
     */
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

    /**
     * 입력된 채팅방 이름에 따른 채팅방 이름 변경
     *
     * @param chat 이름이 변경될 채팅방
     * @param userId 변경 요청한 유저 id
     * @param newName 변경될 이름
     * @return 응답 결과 + 채팅 기능 결과 데이터
     */
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

    //////////////////////////////////

    /**
     * 채팅 닉네임 요청 및 취소 api
     *
     * @param chatId 닉네임 요청 및 취소 한 채팅방 id
     * @param type 요청 인지 취소인지 확인하기 위한 값
     * @param request 유저 정보 추출 용도
     * @return 응답 결과 + 채팅 기능 결과 데이터 (ok시)
     */
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

    /**
     * 채팅방 닉네임 요청 또는 취소에 따른 처리
     *
     * @param type 요청인지 취소인지 확인하기 위한 값
     * @param chat 요청 및 취소 한 채팅방
     * @param userId 요청 및 취소한 유저 Id
     * @return 응답 결과 + 채팅 기능 결과 데이터 (ok시)
     */
    private ApiResponse<?> nicknameRequestProcessing(String type, Chat chat, Long userId) {
        if (type.equals(REQUEST.get())) {
            return requestTypeProcessing(chat, userId);
        } else if (type.equals(CANCEL.get())) {
            return cancelTypeProcessing(chat, userId);
        } else {
            return ApiResponse.statusAndMessage(HttpStatus.BAD_REQUEST, NOT_RIGHT_TYPE.get());
        }
    }

    /**
     * 닉네임 요청 상태에 따른 채팅방 닉네임 요청 처리
     *
     * @param chat 닉네임 요청한 채팅방
     * @param userId 닉네임 요청한 유저 Id
     * @return 응답 결과 + 채팅 기능 결과 데이터 (ok시)
     */
    private ApiResponse<?> requestTypeProcessing(Chat chat, Long userId) {
        if (chat.getNicknameOpen() == NICKNAME_NO_OPEN_STATE.get()) {
            chatService.changeNicknameOpen(chat, NICKNAME_REQUEST_STATE.get());
            String nickname = getNickname(chat, userId);
            String text = "[" + nickname + "] 님이 닉네임을 요청하였습니다.";
            ChatFuncResultDTO chatFuncResultDTO = new ChatFuncResultDTO(REQUEST.get(), text);
            return ApiResponse.ok(chatFuncResultDTO);
        } else {
            return ApiResponse.statusAndMessage(HttpStatus.CONFLICT, ALREADY_PROCESSED.get());
        }
    }

    /**
     * 닉네임 요청 상태에 따른 채팅방 닉네임 요청 취소 처리
     *
     * @param chat 닉네임 요청 취소한 채팅방
     * @param userId 닉네임 요청한 유저 Id
     * @return 응답 결과 + 채팅 기능 결과 데이터 (ok시)
     */
    private ApiResponse<?> cancelTypeProcessing(Chat chat, Long userId) {
        if (chat.getNicknameOpen() == NICKNAME_REQUEST_STATE.get()) {
            chatService.changeNicknameOpen(chat, NICKNAME_NO_OPEN_STATE.get());
            String nickname = getNickname(chat, userId);
            String text = "[" + nickname + "] 님이 닉네임을 요청을 취소하였습니다.";
            ChatFuncResultDTO chatFuncResultDTO = new ChatFuncResultDTO(CANCEL.get(), text);
            return ApiResponse.ok(chatFuncResultDTO);
        } else {
            return ApiResponse.statusAndMessage(HttpStatus.BAD_REQUEST, NO_NICKNAME_REQUEST_TO_CANCEL.get());
        }
    }

    //////////////////////////////////

    /**
     * 채팅 닉네임 수락 및 거절 api
     *
     * @param chatId 닉네임 수락 및 거절 한 채팅방 id
     * @param type 수락 인지 거절인지 확인하기 위한 값
     * @param request 유저 정보 추출 용도
     * @return 응답 결과 + 채팅 기능 결과 데이터 (ok시)
     */
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

    /**
     * 채팅방 닉네임 수락 또는 거절에 따른 처리
     *
     * @param type 수락인지 거절인지 확인하기 위한 값
     * @param chat 수락 및 거절한 채팅방
     * @param userId 수락 및 거절한 유저 Id
     * @return 응답 결과 + 채팅 기능 결과 데이터 (ok시)
     */
    private ApiResponse<?> nicknameResponseProcessing(String type, Chat chat, Long userId) {
        if (type.equals(ACCEPT.get())) {
            return acceptTypeProcessing(chat, userId);
        } else if (type.equals(REJECT.get())) {
            return rejectTypeProcessing(chat, userId);
        } else {
            return ApiResponse.statusAndMessage(HttpStatus.BAD_REQUEST, NOT_RIGHT_TYPE.get());
        }
    }

    /**
     * 채팅방 닉네임 수락 처리
     *
     * @param chat 닉네임 요청 수락한 채팅방
     * @param userId 닉네임 요청 수락한 유저 Id
     * @return 응답 결과 + 채팅 기능 결과 데이터
     */
    private ApiResponse<?> acceptTypeProcessing(Chat chat, Long userId) {
        chatService.changeNicknameOpen(chat, NICKNAME_OPEN_STATE.get());
        String nickname = getNickname(chat, userId);
        String text = "[" + nickname + "] 님이 닉네임 요청을 수락하셨습니다.";
        ChatFuncResultDTO chatFuncResultDTO = new ChatFuncResultDTO(ACCEPT.get(), text);
        return ApiResponse.ok(chatFuncResultDTO);
    }

    /**
     * 채팅방 닉네임 거절 처리
     *
     * @param chat 닉네임 요청 거절한 채팅방
     * @param userId 닉네임 요청 거절한 유저 Id
     * @return 응답 결과 + 채팅 기능 결과 데이터
     */
    private ApiResponse<?> rejectTypeProcessing(Chat chat, Long userId) {
        chatService.changeNicknameOpen(chat, NICKNAME_NO_OPEN_STATE.get());
        String nickname = getNickname(chat, userId);
        String text = "[" + nickname + "] 님이 닉네임 요청을 거절하셨습니다.";
        ChatFuncResultDTO chatFuncResultDTO = new ChatFuncResultDTO(REJECT.get(), text);
        return ApiResponse.ok(chatFuncResultDTO);
    }

    //////////////////////////////////

    /**
     * 질문 존재 여부 확인<br/>
     * 유저의 채팅 생성 권한 여부 확인
     *
     * @param question 채팅 생성 위한 질문
     * @param userId 생성 요청한 유저 Id
     * @return 응답 결과
     */
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

    /**
     * 채팅방 존재 여부 확인<br/>
     * 유저의 채팅 접근 가능 여부 확인
     *
     * @param chat 접근할 채팅방
     * @param userId 접근 요청한 유저 Id
     * @return 응답 결과
     */
    private ApiResponse<?> validateChatAndUser(Chat chat, Long userId) {
        if (chat == null) {
            return ApiResponse.statusAndMessage(HttpStatus.NOT_FOUND, CANNOT_FIND_CHAT.get());
        }

        if (!chat.isAccessibleUser(userId)) {
            return ApiResponse.statusAndMessage(HttpStatus.FORBIDDEN, NO_HAVE_CHAT_PERMISSION.get());
        }

        return ApiResponse.ok();
    }

    /**
     * 채팅방 존재 여부 확인<br/>
     * 유저의 닉네임 요청 및 취소 가능 여부 확인
     *
     * @param chat 닉네임 요청 및 취소할 채팅방
     * @param userId 요청 및 취소한 유저 Id
     * @return 응답 결과
     */
    private ApiResponse<?> validateChatAndNicknameRequest(Chat chat, Long userId) {
        if (chat == null) {
            return ApiResponse.statusAndMessage(HttpStatus.NOT_FOUND, CANNOT_FIND_CHAT.get());
        }

        if (!chat.isAnswerer(userId)) {
            return ApiResponse.statusAndMessage(HttpStatus.FORBIDDEN, NO_HAVE_REQUEST_NICKNAME_PERMISSION.get());
        }

        return ApiResponse.ok();
    }

    /**
     * 채팅방 존재 여부 확인<br/>
     * 유저의 닉네임 응답 가능 여부 확인<br/>
     * 닉네임 요청 상태에 따른 응답 가능 여부 확인
     *
     * @param chat 닉네임 요청 응답할 채팅방
     * @param userId 응답한 유저 Id
     * @return 응답 결과
     */
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

    //////////////////////////////////

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
}
