package bokjak.bokjakserver.domain.report.model;

import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.domain.report.exception.ReportException;

public enum ReportTarget {
    SPOT,COMMENT;

    public static ReportTarget toEnum(String reportTarget) {
        return switch (reportTarget.toUpperCase()) {
            case "SPOT" -> SPOT;
            case "COMMENT" -> COMMENT;
            default -> throw new ReportException(StatusCode.NOT_FOUND_REPORT_TARGET);
        };
    }
}
