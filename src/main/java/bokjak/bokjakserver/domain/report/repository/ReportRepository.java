package bokjak.bokjakserver.domain.report.repository;

import bokjak.bokjakserver.domain.report.model.Report;
import bokjak.bokjakserver.domain.report.model.ReportTarget;
import bokjak.bokjakserver.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    boolean existsByReporterAndReportedUserAndReportTargetAndTargetId(User reporterUser, User reportedUser, ReportTarget reportTarget, Long TargetId);

}
