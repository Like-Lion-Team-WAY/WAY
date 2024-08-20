package like.lion.way.admin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import like.lion.way.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reports")
@Getter
@NoArgsConstructor // for JPA
public class Report {
    @Id
    @Column(name = "report_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "report_reporter")
    private User reporter;

    @ManyToOne
    @JoinColumn(name = "report_reported")
    private User reported;

    @Column(name = "report_type")
    private String type; // 중간 테이블 만들까?

    @Column(name = "report_content_id")
    private String contentId;

    @Column(name = "report_content")
    private String content;

    @Column(name = "report_created_at")
    private Timestamp createdAt;

    @Column(name = "report_status")
    private Boolean status;

    public Report(User reporter, User reported, String type, String contentId, String content) {
        this.reporter = reporter;
        this.reported = reported;
        this.type = type;
        this.contentId = contentId;
        this.content = content;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.status = false;
    }
}
