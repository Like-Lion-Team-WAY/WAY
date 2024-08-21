package like.lion.way.chat.constant;

public enum ChatMessageType {
    CREATE("create"),
    EXIST("exist"),
    OPEN("open"),
    CLOSE("close"),
    LEAVE("leave"),
    DELETE("delete"),
    NO_CHANGE("noChange"),
    CHANGE("change"),
    REQUEST("request"),
    CANCEL("cancel"),
    ACCEPT("accept"),
    REJECT("reject");

    private final String type;

    ChatMessageType(String type) {
        this.type = type;
    }

    public String get() {
        return type;
    }
}
