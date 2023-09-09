package bokjak.bokjakserver.domain.report.service;

import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.domain.comment.model.Comment;
import bokjak.bokjakserver.domain.comment.repository.CommentRepository;
import bokjak.bokjakserver.domain.report.dto.ReportDto;
import bokjak.bokjakserver.domain.report.dto.ReportDto.ReportIdResponse;
import bokjak.bokjakserver.domain.report.dto.ReportDto.ReportRequest;
import bokjak.bokjakserver.domain.report.exception.ReportException;
import bokjak.bokjakserver.domain.report.model.Report;
import bokjak.bokjakserver.domain.report.model.ReportTarget;
import bokjak.bokjakserver.domain.report.repository.ReportRepository;
import bokjak.bokjakserver.domain.spot.model.Spot;
import bokjak.bokjakserver.domain.spot.repository.SpotRepository;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final SpotRepository spotRepository;

    @Transactional
    public ReportIdResponse createReport(Long userId, ReportRequest reportRequest) {
        User reporter = userRepository.findById(userId).orElseThrow(() -> new ReportException(StatusCode.NOT_FOUND_USER));
        User reportedUser = userRepository.findById(reportRequest.reportedUserId()).orElseThrow(() -> new ReportException(StatusCode.NOT_FOUND_REPORTED_USER));
        checkContentLength(reportRequest);
        checkIsReport(reportedUser);
        Report report = reportRequest.toEntity(reporter, reportedUser);
        checkExistSpotOrComment(report.getReportTarget(), report.getTargetId(), reportedUser);
        boolean exists = reportRepository.existsByReporterAndReportedUserAndReportTargetAndTargetIdAndIsCheckedNull(
                reporter, reportedUser, report.getReportTarget(), report.getTargetId());
        if (exists) throw new ReportException(StatusCode.REPORT_DUPLICATION);

        reportRepository.save(report);
        reporter.addReporterUser(report);
        reportedUser.addReportedUser(report);

        return ReportIdResponse.of(report);
    }


    private void checkExistSpotOrComment(ReportTarget reportTarget, Long targetId, User reportedUser) {

        switch (reportTarget) {
            case SPOT :
                Spot spot = spotRepository.findById(targetId).orElseThrow(() -> new ReportException(StatusCode.NOT_FOUND_REPORT_TARGET));
                if (!spot.getUser().equals(reportedUser)) throw new ReportException(StatusCode.NOT_CORRECT_USER_AND_TARGET);
                break;
            case COMMENT:
                Comment comment = commentRepository.findById(targetId).orElseThrow(() -> new ReportException(StatusCode.NOT_FOUND_REPORT_TARGET));
                if (!comment.getUser().equals(reportedUser)) throw new ReportException(StatusCode.NOT_CORRECT_USER_AND_TARGET);
        }
    }

    private void checkIsReport(User user) {
        switch (user.getUserStatus()) {
            case BANNED:
                new ReportException(StatusCode.ALREADY_BAN_USER);
            case BLACKLIST:
                new ReportException(StatusCode.ALREADY_BAN_USER);
            default:
                break;
        }
    }

    private void checkContentLength(ReportRequest reportRequest) {
        if (reportRequest.content().length() > 300) throw new ReportException(StatusCode.OVER_CONTENT_LENGTH);
    }
}
