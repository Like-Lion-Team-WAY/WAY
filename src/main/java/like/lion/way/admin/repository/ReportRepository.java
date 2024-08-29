package like.lion.way.admin.repository;

import java.util.List;
import like.lion.way.admin.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>, JpaSpecificationExecutor<Report> {
    List<Report> findByType(String type);
    List<Report> findByReported_Username(String username);

}
