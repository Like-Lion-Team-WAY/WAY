package like.lion.way.user.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import like.lion.way.user.domain.Block;
import like.lion.way.user.domain.User;
import org.springframework.http.ResponseEntity;

public interface BlockService {
    List<String> getBlcokList(User user);

    ResponseEntity<?> unblock(HttpServletRequest request, String username);
    List<?> blockFilter(List<?> checkContents,HttpServletRequest request);
    Block findByUser(User user , HttpServletRequest request);
}
