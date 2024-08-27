package like.lion.way.exception;

import com.amazonaws.services.kms.model.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DisabledException.class)
    public String handleDisabledException(DisabledException ex, RedirectAttributes redirectAttributes) {
        System.out.println("123123");
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/user/login";  // 로그인 페이지로 리다이렉트
    }
}
