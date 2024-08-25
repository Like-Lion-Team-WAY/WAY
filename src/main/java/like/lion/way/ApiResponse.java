package like.lion.way;

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

    public ApiResponse(
            HttpStatus status,
            String message,
            T data) {

        this.code = status.value();
        this.status = status;
        this.message = message;
        this.success = (status.is2xxSuccessful());
        this.data = data;

    }


    public static <T> ApiResponse<T> ok(T data) {

        return new ApiResponse<>(HttpStatus.OK, HttpStatus.OK.name(), data);

    }

    public static ApiResponse<Void> ok() {

        return new ApiResponse<>(HttpStatus.OK, HttpStatus.OK.name(), null);

    }

    public static ApiResponse<Void> status(HttpStatus status) {

        return new ApiResponse<>(status, status.name(), null);

    }

    public static <T> ApiResponse<T> statusAndData(HttpStatus status, T data) {

        return new ApiResponse<>(status, status.name(), data);

    }

    public static ApiResponse<Void> statusAndMessage(HttpStatus status, String message) {

        return new ApiResponse<>(status, message, null);

    }

    public static <T> ApiResponse<T> statusAndAll(HttpStatus status, String message, T data) {

        return new ApiResponse<>(status, message, data);

    }

}
