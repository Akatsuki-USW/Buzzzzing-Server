package bokjak.bokjakserver.domain.report.dto;

import bokjak.bokjakserver.domain.report.model.Report;
import bokjak.bokjakserver.domain.report.model.ReportTarget;
import bokjak.bokjakserver.domain.user.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

public class ReportDto {

    public record ReportRequest(
            @Schema(example = "SPOT")
            @NotEmpty
            String reportTarget,
            @Schema(example = "333")
            @Positive
            Long reportTargetId,
            @Schema(example = "12")
            @Positive
            Long reportedUserId,
            @Schema(example = "부적절한 언어를 사용해요")
            @NotEmpty
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
                    .isChecked(false)
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
