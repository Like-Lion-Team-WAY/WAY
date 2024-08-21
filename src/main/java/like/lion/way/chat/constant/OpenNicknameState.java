package like.lion.way.chat.constant;

public enum OpenNicknameState {
    NICKNAME_NO_OPEN_STATE(0),
    NICKNAME_REQUEST_STATE(1),
    NICKNAME_OPEN_STATE(2);

    private final int state;

    OpenNicknameState(int state) {
        this.state = state;
    }

    public int get() {
        return state;
    }
}
