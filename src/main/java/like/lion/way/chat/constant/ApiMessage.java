package like.lion.way.chat.constant;

public enum ApiMessage {
    OK("이용 가능"),

    CANNOT_FIND_QUESTION("해당 질문을 찾을 수 없습니다."),
    CANNOT_FIND_CHAT("채팅방을 찾을 수 없습니다."),
    NO_HAVE_CHAT_PERMISSION("해당 채팅방에 대한 권한이 없습니다"),
    NO_HAVE_CREATE_CHAT_PERMISSION("채팅방 생성 권한이 없습니다"),
    NO_HAVE_REQUEST_NICKNAME_PERMISSION("닉네임 요청에 대한 권한이 없습니다."),
    NO_HAVE_ACCEPT_NICKNAME_PERMISSION("닉네임 수락에 대한 권한이 없습니다."),
    NO_NICKNAME_REQUEST("닉네임에 대한 요청이 없습니다."),
    NO_NICKNAME_REQUEST_TO_REJECT("취소할 요청이 없습니다."),
    CANNOT_CHAT_WITH_NON_MEMBER("비회원의 질문에 대해서 채팅은 불가합니다."),
    ALREADY_PROCESSED("이미 처리된 요청입니다."),
    NOT_RIGHT_TYPE("적절한 타입이 아닙니다."),
    NO_HAVE_MESSAGE_PERMISSION("해당 메세지에 대한 접근 권한이 없습니다.");

    private String message;

    ApiMessage(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}
