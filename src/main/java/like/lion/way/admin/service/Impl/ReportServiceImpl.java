package like.lion.way.admin.service.Impl;

import like.lion.way.admin.domain.Report;
import like.lion.way.admin.domain.ReportRequestDto;
import like.lion.way.admin.domain.ReportType;
import like.lion.way.admin.repository.ReportRepository;
import like.lion.way.admin.service.ReportService;
import like.lion.way.feed.service.PostCommentService;
import like.lion.way.feed.service.PostService;
import like.lion.way.feed.service.QuestionService;
import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
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

        if (type == ReportType.QUESTION) {
            var value = questionService.getQuestionById(reportRequestDto.getId());
            reported = value.getQuestioner();
            content = value.getQuestion();
        } else if (type == ReportType.POST) {
            var value = postService.getPostById(reportRequestDto.getId());
            reported = value.getUser();
            content = "[" + value.getPostTitle() + "]" + value.getPostContent();
        } else if (type == ReportType.COMMENT) {
            var value = postCommentService.getCommentById(reportRequestDto.getId());
            reported = value.getUser();
            content = value.getPostCommentContent();
        } else if (type == ReportType.CHATTING) {
            reported = null;
            content = null;
        } else {
            throw new IllegalArgumentException("Invalid report type");
        }

        return new Report(reporter, reported, type.toString(), reportRequestDto.getId(), content);
    }
}
