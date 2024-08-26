package like.lion.way.admin.repository;

import like.lion.way.admin.domain.Report;
import org.springframework.data.jpa.domain.Specification;

public class ReportSpecification {
    public static Specification<Report> typeEqual(String type) {
        return (root, query, cb) -> cb.equal(root.get("type"), type);
    }
}
