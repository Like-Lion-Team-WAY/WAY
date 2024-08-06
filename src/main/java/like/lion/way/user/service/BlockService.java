package like.lion.way.user.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface BlockService {
    List<String> getBlcokList(HttpServletRequest request);
}
