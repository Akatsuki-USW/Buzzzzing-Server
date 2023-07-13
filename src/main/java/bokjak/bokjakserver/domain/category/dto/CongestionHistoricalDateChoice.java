package bokjak.bokjakserver.domain.category.dto;

import bokjak.bokjakserver.common.constant.GlobalConstants;
import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.domain.category.exception.CategoryException;
import bokjak.bokjakserver.util.enums.EnumModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Getter
@AllArgsConstructor
public enum CongestionHistoricalDateChoice implements EnumModel<String> {
    MON("월요일"), TUE("화요일"), WED("수요일"), THU("목요일"),
    FRI("금요일"), SAT("토요일"), SUN("일요일");

    private final String value;

    public static CongestionHistoricalDateChoice toEnum(String stringParam) {
        return switch (stringParam.toUpperCase()) {
            case "MON" -> MON;
            case "TUE" -> TUE;
            case "WED" -> WED;
            case "THU" -> THU;
            case "FRI" -> FRI;
            case "SAT" -> SAT;
            case "SUN" -> SUN;

            default -> throw new CategoryException(StatusCode.CHOICE_NOT_EXIST);
        };
    }

    // toDateTime: congestionDateFilter에 대해 요청된 요일(name 필드)에 따라 값을 다르게 변환. SQL 날짜 형식에 맞도록 포맷
    public static String toDateTime(CongestionHistoricalDateChoice choice) {
        Calendar calendar = Calendar.getInstance();
        if (choice != SUN) calendar.add(Calendar.DATE, -GlobalConstants.WEEK_SIZE);// 일주일 전

        switch (choice) {
            case MON -> calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            case TUE -> calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
            case WED -> calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
            case THU -> calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
            case FRI -> calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
            case SAT -> calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            case SUN -> calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

            default -> throw new IllegalArgumentException(StatusCode.INTERNAL_SERVER_ERROR.getMessage());  // Internal Error 이여야 함
        }

        SimpleDateFormat formatter = new SimpleDateFormat(GlobalConstants.DATE_FORMAT);
        return formatter.format(calendar.getTime());
    }

    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return value;
    }

}
