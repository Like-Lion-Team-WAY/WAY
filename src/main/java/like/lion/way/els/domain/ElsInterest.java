package like.lion.way.els.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;


@Document(indexName = "interests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ElsInterest {

    @Id
    private String id;
    private String name;
    private String description;


}
