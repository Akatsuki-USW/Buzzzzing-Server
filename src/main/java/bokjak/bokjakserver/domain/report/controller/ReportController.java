package bokjak.bokjakserver.domain.report.controller;

import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.domain.report.dto.ReportDto;
import bokjak.bokjakserver.domain.report.service.ReportService;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static bokjak.bokjakserver.common.dto.ApiResponse.success;
import static bokjak.bokjakserver.domain.report.dto.ReportDto.*;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final UserService userService;
    private final ReportService reportService;

    @PostMapping("/reports")
    public ApiResponse<?> createReport(@RequestBody ReportRequest reportRequest) {
        User currentUser = userService.getCurrentUser();
        return success(reportService.createReport(currentUser, reportRequest));
    }
}
