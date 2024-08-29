package like.lion.way.admin.dto;

import like.lion.way.admin.domain.ReportType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequestDto {
    private ReportType type;
    private String id;
}
