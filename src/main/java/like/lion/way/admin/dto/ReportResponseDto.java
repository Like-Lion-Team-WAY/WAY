package like.lion.way.admin.dto;

import like.lion.way.admin.domain.Report;
import lombok.Getter;

@Getter
public class ReportResponseDto {
    private Long id;
    private String reporter;
    private String reported;
    private String type;
    private String contentId;
    private String content;
    private String createdAt;

    public ReportResponseDto(Report report) {
        this.id = report.getId();
        this.reporter = report.getReporter().getUsername();
        this.reported = report.getReported().getUsername();
        this.type = report.getType();
        this.contentId = report.getContentId();
        this.content = report.getContent();
        this.createdAt = report.getCreatedAt().toString();
    }
}
