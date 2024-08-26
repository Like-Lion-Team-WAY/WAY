package like.lion.way.admin.service.Impl;

import java.util.List;
import like.lion.way.admin.domain.Report;
import like.lion.way.admin.dto.ReportRequestDto;
import like.lion.way.admin.domain.ReportType;
import like.lion.way.admin.dto.ReportResponseDto;
import like.lion.way.admin.repository.ReportRepository;
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

    private final UserService userService;
    private final QuestionService questionService;
    private final PostService postService;
    private final PostCommentService postCommentService;
    private final MessageService messageService;

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

    @Override
    public List<ReportResponseDto> getReports(String type, String reportedUsername, String sortDirection) {
        User reported = userService.findByUsername(reportedUsername);
        Specification<Report> spec = Specification.where(ReportSpecification.hasStatus(false))
                .and(ReportSpecification.hasType(type))
                .and(ReportSpecification.hasReportedUser(reported))
                .and(ReportSpecification.sortByCreatedAt(sortDirection));
        return reportRepository.findAll(spec).stream().map(ReportResponseDto::new).toList();
    }
}
