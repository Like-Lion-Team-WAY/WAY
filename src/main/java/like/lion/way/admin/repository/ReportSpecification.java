package like.lion.way.admin.repository;

import like.lion.way.admin.domain.Report;
import like.lion.way.user.domain.User;
import org.springframework.data.jpa.domain.Specification;

public class ReportSpecification {
    public static Specification<Report> hasStatus(Boolean status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<Report> hasType(String type) {
        // toPredicate 메서드를 구현하는 람다식
        return (root, query, criteriaBuilder) ->{
            if (type == null || type.equals("ALL")) {
                return criteriaBuilder.conjunction(); // 조건 없이 모두 선택
            }
            return criteriaBuilder.equal(root.get("type"), type);
        };
    }

    public static Specification<Report> hasReportedUser(User reported) {
        return (root, query, criteriaBuilder) -> {
            if (reported == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("reported"), reported);
        };
    }

    public static Specification<Report> sortByCreatedAt(String sortDirection) {
        return (root, query, criteriaBuilder) -> {
            if (sortDirection == null || sortDirection.equals("ASC")) {
                query.orderBy(criteriaBuilder.asc(root.get("createdAt")));
            } else {
                query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
            }
            return criteriaBuilder.conjunction();
        };
    }
}
