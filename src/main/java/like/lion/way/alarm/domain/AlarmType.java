package like.lion.way.alarm.domain;

public enum AlarmType {
    COMMENT("COMMENT", " 님이 회원님의 글에 댓글을 달았습니다.", "/posts/detail/"),
    NEW_QUESTION("NEW_QUESTION", "새 질문이 도착했습니다.", "/questions/NewList/"),
    ANSWER("ANSWER", " 님이 회원님이 남긴 질문에 답변을 달았습니다.", ""),
    REPLY("REPLY", " 님이 회원님이 남긴 댓글에 대댓글을 달았습니다.", "/posts/detail/"),
    BOARD_COMMENT("BOARD_COMMENT", " 님이 회원님의 커뮤니티 게시글에 댓글을 달았습니다.", "")
    ;

    private final String type;
    private final String message;
    private final String url;

    AlarmType(String type, String message, String url) {
        this.type = type;
        this.message = message;
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public String getMessage(String username) {
        return username + message;
    }

    public String getUrl(String pathVariable) {
        return url + pathVariable;
    }


}
