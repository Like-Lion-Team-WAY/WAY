package like.lion.way.admin.repository;

import java.util.List;
import like.lion.way.admin.domain.Report;

public interface ReportRepositoryCustom {
    List<Report> findReportsByCriteria(String type, String reported, String sortDirection);
}
