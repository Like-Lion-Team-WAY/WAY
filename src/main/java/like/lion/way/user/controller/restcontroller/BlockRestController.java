package like.lion.way.user.controller.restcontroller;

import jakarta.servlet.http.HttpServletRequest;
import like.lion.way.user.service.BlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("block")
public class BlockRestController {
    private final BlockService blockService;

    @DeleteMapping("/unblock")
    public ResponseEntity<?> unblock(HttpServletRequest request , @RequestParam("username") String username){
        return blockService.unblock(request,username);
    }
}
