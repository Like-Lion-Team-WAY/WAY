package like.lion.way;

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

    // Static factory method for initiating the builder
    public static <T> ApiResponseBuilder<T> status(HttpStatus status) {
        return new ApiResponseBuilder<>(status);
    }

    public static class ApiResponseBuilder<T> {
        private HttpStatus status;
        private String message;
        private T data;

        public ApiResponseBuilder(HttpStatus status) {
            this.status = status;
        }

        public ApiResponseBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public ApiResponseBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ApiResponse<T> build() {
            return new ApiResponse<>(status, message, data);
        }
    }

}
