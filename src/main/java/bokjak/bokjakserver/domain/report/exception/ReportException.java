package bokjak.bokjakserver.domain.report.exception;

import bokjak.bokjakserver.common.exception.BuzException;
import bokjak.bokjakserver.common.exception.StatusCode;

public class ReportException extends BuzException {
    public ReportException(StatusCode statusCode) {
        super(statusCode);
    }
}
