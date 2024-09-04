package like.lion.way.admin.service.Impl;

import java.util.List;
import like.lion.way.admin.domain.Report;
import like.lion.way.admin.dto.ReportRequestDto;
import like.lion.way.admin.domain.ReportType;
import like.lion.way.admin.dto.ReportResponseDto;
import like.lion.way.admin.repository.ReportRepository;
import like.lion.way.admin.repository.ReportRepositoryCustom;
import like.lion.way.admin.repository.ReportSpecification;
import like.lion.way.admin.service.ReportService;
import like.lion.way.chat.service.MessageService;
import like.lion.way.feed.service.PostCommentService;
import like.lion.way.feed.service.PostService;
import like.lion.way.feed.service.QuestionService;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // for constructor injection
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final ReportRepositoryCustom reportRepositoryCustom;

    private final UserService userService;
    private final QuestionService questionService;
    private final PostService postService;
    private final PostCommentService postCommentService;
    private final MessageService messageService;

    /**
     * 신고 신청
     * @param reporterId 신고자 id
     * @param reportRequestDto 신고 신청 정보
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void report(Long reporterId, ReportRequestDto reportRequestDto) {
        try {
            Report report = createReportByType(reporterId, reportRequestDto);
            reportRepository.save(report);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 신고 객체 생성 (QUESTION, POST, COMMENT, CHATTING)
     * @param reporterId 신고자 id
     * @param reportRequestDto 신고 신청 정보
     * @return 생성된 신고 객체
     */
    public Report createReportByType(Long reporterId, ReportRequestDto reportRequestDto) {
        ReportType type = reportRequestDto.getType();
        User reporter = userService.findByUserId(reporterId);
        User reported;
        String content;

        switch (type) {
            case QUESTION -> {
                var value = questionService.getQuestionById(Long.valueOf(reportRequestDto.getId()));
                reported = value.getQuestioner();
                content = value.getQuestion();
            }
            case POST -> {
                var value = postService.getPostById(Long.valueOf(reportRequestDto.getId()));
                reported = value.getUser();
                content = "[" + value.getPostTitle() + "]" + value.getPostContent();
            }
            case COMMENT -> {
                var value = postCommentService.getCommentById(Long.valueOf(reportRequestDto.getId()));
                reported = value.getUser();
                content = value.getPostCommentContent();
            }
            case CHATTING -> {
                var value = messageService.findById(reportRequestDto.getId());
                reported = userService.findByUserId(value.getSenderId());
                content = value.getText();
            }
            default -> throw new IllegalArgumentException("Invalid report type");
        }
        return new Report(reporter, reported, type.toString(), reportRequestDto.getId(), content);
    }

    /**
     * 신고 신청 목록 조회
     * @param type 신고 타입
     * @param reportedUsername 신고 당한 사용자
     * @param sortDirection 정렬 방향
     * @return 필터링 된 신고 신청 목록
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReportResponseDto> getReports(String type, String reportedUsername, String sortDirection) {
        var list = reportRepositoryCustom.findReportsByCriteria(type, reportedUsername, sortDirection);
        return list.stream().map(ReportResponseDto::new).toList();
    }

    /**
     * 신고 삭제
     * @param id 삭제할 신고 id
     */
    @Override
    @Transactional
    public void deleteReport(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid report id"));
        report.complete();
    }
}
