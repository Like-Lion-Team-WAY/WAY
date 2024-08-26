package like.lion.way.els.domain;
import jakarta.persistence.Id;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "users")
public class ElsUser {
    @Id
    private String id;
    private String username;
    private String imageUrl;
    private List<String> interests;
}
