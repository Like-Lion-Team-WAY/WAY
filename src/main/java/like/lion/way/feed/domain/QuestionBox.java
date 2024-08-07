package like.lion.way.feed.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import like.lion.way.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "question_box")
@Getter
@Setter
public class QuestionBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionBoxId;

    @OneToMany(mappedBy = "questionBox")
    private Set<Question> questions;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
