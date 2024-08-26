package like.lion.way.admin.service;

import java.util.List;
import like.lion.way.admin.dto.ReportRequestDto;
import like.lion.way.admin.dto.ReportResponseDto;

public interface ReportService {
    void report(Long reporterId, ReportRequestDto reportRequestDto);

    List<ReportResponseDto> getReports(String type, String reportedUsername, String sortDirection);
    void deleteReport(Long id);
}
