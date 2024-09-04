package like.lion.way.chat.controller.rest;

import static like.lion.way.chat.constant.ApiMessage.NO_HAVE_MESSAGE_PERMISSION;
import static like.lion.way.chat.constant.OpenNicknameState.NICKNAME_OPEN_STATE;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import like.lion.way.ApiResponse;
import like.lion.way.chat.domain.Chat;
import like.lion.way.chat.domain.Message;
import like.lion.way.chat.dto.OldMessageDTO;
import like.lion.way.chat.dto.ReceiveMessageDTO;
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

/**
 * 채팅 메세지에 대한 api controller
 *
 * @author Lee NaYeon
 */
@RestController
@RequiredArgsConstructor
public class MessageRestController {
    private final ChatService chatService;
    private final MessageService messageService;
    private final JwtUtil jwtUtil;

    /**
     * 유저 권한에 따른 페이지네이션 처리한 이전 메세지 불러오기 api
     *
     * @param chatId 메세지 불러올 채팅방 Id
     * @param page 불러올 페이지 번호
     * @param size 불러올 메세지 개수
     * @param lastLoadMessageId 이전에 마지막으로 불러간 메세지 Id
     * @param request 유저 정보 추출 용도
     * @return 응답 결과 + 메세지 데이터 (ok 시)
     */
    @GetMapping("/api/messages/{chatId}")
    public ApiResponse<?> getMessages(@PathVariable("chatId") Long chatId,
                                      @RequestParam(name = "page", defaultValue = "1") int page,
                                      @RequestParam(name = "size", defaultValue = "30") int size,
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

    /**
     * 이전에 불러간 메세지 여부에 따른 메세지 추출
     *
     * @param page 불러올 페이지 번호
     * @param size 불러올 메세지 개수
     * @param chatId 메세지 불러올 채팅방 Id
     * @param lastLoadMessageId 이전에 마지막으로 불러간 메세지 Id
     * @return 메세지 데이터
     */
    private Page<Message> findMessages(int page, int size, Long chatId, String lastLoadMessageId) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());

        if (!lastLoadMessageId.isEmpty()) {
            return messageService.findAllByChatIdAndIdLessThan(chatId, lastLoadMessageId, pageable);
        } else {
            return messageService.findAllByChatId(chatId, pageable);
        }

    }

    /**
     * Message 페이지 데이터 -> ReceiveMessageDTO 로 변환
     *
     * @param messages 변환할 message page
     * @param chat 메세지가 소속된 채팅방
     * @return 변환된 ReceiveMessageDTO 리스트
     */
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

    /**
     * 유저 Id 추출
     *
     * @param request 유저 정보 추출 용도
     * @return 유저 Id
     */
    private Long getUserId(HttpServletRequest request) {
        String token = jwtUtil.getCookieValue(request, "accessToken");
        return jwtUtil.getUserIdFromToken(token);
    }
}
