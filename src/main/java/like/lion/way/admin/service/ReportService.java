package like.lion.way.admin.service;

import like.lion.way.admin.domain.ReportRequestDto;
import like.lion.way.user.domain.User;

public interface ReportService {
void report(Long reporterId, ReportRequestDto reportRequestDto);
}
