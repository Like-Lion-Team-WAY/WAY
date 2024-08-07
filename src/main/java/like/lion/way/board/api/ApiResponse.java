package like.lion.way.board.api;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {

    private int code;
    private HttpStatus status;
    private String message;
    private boolean success;
    private T data;

    @Builder
    public ApiResponse(
            HttpStatus status,
            String message,
            T data) {

        this.code = status.value();
        this.status = status;
        this.message = message;
        this.success = true;
        this.data = data;

    }

    public static <T> ApiResponse<T> ok(T data) {

        return new ApiResponse<>(HttpStatus.OK, HttpStatus.OK.name(), data);

    }

    public static ApiResponse<Void> ok() {

        return new ApiResponse<>(HttpStatus.OK, HttpStatus.OK.name(), null);

    }

}
