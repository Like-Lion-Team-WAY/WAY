package like.lion.way.admin.repository;

import java.util.List;
import like.lion.way.admin.domain.Report;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ReportRepositoryCustomImpl implements ReportRepositoryCustom {
    private final ReportRepository reportRepository;
    private final UserService userService;

    @Override
    public List<Report> findReportsByCriteria(String type, String reportedUsername, String sortDirection) {
        User reported = userService.findByUsername(reportedUsername);

        Specification<Report> spec = Specification.where(ReportSpecification.hasStatus(false))
                .and(ReportSpecification.hasType(type))
                .and(ReportSpecification.hasReportedUser(reported))
                .and(ReportSpecification.sortByCreatedAt(sortDirection));

        return reportRepository.findAll(spec);
    }
}
