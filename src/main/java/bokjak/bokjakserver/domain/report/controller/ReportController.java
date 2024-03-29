package bokjak.bokjakserver.domain.report.controller;

import bokjak.bokjakserver.common.constant.SwaggerConstants;
import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.config.security.PrincipalDetails;
import bokjak.bokjakserver.domain.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static bokjak.bokjakserver.common.constant.SwaggerConstants.*;
import static bokjak.bokjakserver.common.dto.ApiResponse.success;
import static bokjak.bokjakserver.domain.report.dto.ReportDto.*;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = SwaggerConstants.SECURITY_SCHEME_NAME)
@Tag(name = TAG_REPORT, description = TAG_REPORT_DESCRIPTION)
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = REPORT_CREATE, description = REPORT_CREATE_DESCRIPTION)
    @PostMapping("/reports")
    public ApiResponse<?> createReport(@RequestBody @Valid ReportRequest reportRequest,
                                       @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return success(reportService.createReport(principalDetails.getUserId(), reportRequest));
    }
}
