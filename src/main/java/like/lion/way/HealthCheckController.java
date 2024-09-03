package like.lion.way;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @Value("${server.serverName}")
    private String serverName;

    @GetMapping("/hc")
    public ResponseEntity<?> healthCheck(){
        Map<String, String> response = new TreeMap<>();
        response.put("serverName", serverName);
        return ResponseEntity.ok(response);
    }
}
