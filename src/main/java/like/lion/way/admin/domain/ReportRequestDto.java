package like.lion.way.admin.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequestDto {
    private ReportType type;
    private String id;
}
