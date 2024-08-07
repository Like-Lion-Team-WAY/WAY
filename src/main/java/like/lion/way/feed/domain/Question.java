package like.lion.way.feed.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import like.lion.way.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "questions")
@Setter
@Getter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "answer")
    private String answer;

    @Column(name = "question_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime questionDate;

    @Column(name = "answer_date")
    private LocalDateTime answerDate;

    @Column(name = "question_delete_YN", columnDefinition = "TINYINT DEFAULT 0")
    private Boolean questionDeleteYN;

    @Column(name = "question_image_url")
    private String questionImageUrl;

    @Column(name = "question_pin_status", columnDefinition = "TINYINT DEFAULT 0")
    private Boolean questionPinStatus;

    @Column(name = "question_status", columnDefinition = "TINYINT DEFAULT 0")
    private Boolean questionStatus;

    @Column(name = "question_rejected", columnDefinition = "TINYINT DEFAULT 0")
    private Boolean questionRejected;

    @Column(name = "question_like", columnDefinition = "INT DEFAULT 0")
    private Integer questionLike = 0;

    @ManyToOne
    @JoinColumn(name = "questioner_id", referencedColumnName = "user_id")
    private User questioner;

    @ManyToOne
    @JoinColumn(name = "answerer_id", referencedColumnName = "user_id")
    private User answerer;

    @ManyToOne
    @JoinColumn(name = "question_box_id", referencedColumnName = "questionBoxId")
    private QuestionBox questionBox;



}
