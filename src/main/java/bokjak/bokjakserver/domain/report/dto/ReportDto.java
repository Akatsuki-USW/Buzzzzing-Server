package bokjak.bokjakserver.domain.report.dto;

import bokjak.bokjakserver.domain.report.model.Report;
import bokjak.bokjakserver.domain.report.model.ReportTarget;
import bokjak.bokjakserver.domain.user.model.User;
import jakarta.validation.constraints.Max;
import lombok.Builder;

public class ReportDto {

    public record ReportRequest(String reportTarget,
                                Long reportTargetId,
                                Long reportedUserId,
                                String content) {

        @Builder
        public ReportRequest{}

        public Report toEntity(User reporterUser, User reportedUser) {
            return Report.builder()
                    .reporter(reporterUser)
                    .reportedUser(reportedUser)
                    .reportTarget(ReportTarget.toEnum(reportTarget))
                    .targetId(reportTargetId)
                    .content(content)
                    .build();
        }
    }

    public record ReportIdResponse(Long reportId) {

        @Builder
        public ReportIdResponse{}

        public static ReportIdResponse of(Report report) {
            return ReportIdResponse.builder()
                    .reportId(report.getId()).build();
        }
    }


}
