package bokjak.bokjakserver.domain.category.dto;

import bokjak.bokjakserver.common.constant.GlobalConstants;
import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.domain.category.exception.CategoryException;
import bokjak.bokjakserver.util.CustomDateUtils;
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
        Calendar calendar = CustomDateUtils.makePastWeekDayDate(switchChoiceToDayOfWeek(choice));

        SimpleDateFormat formatter = new SimpleDateFormat(GlobalConstants.DATE_FORMAT);
        return formatter.format(calendar.getTime());
    }

    private static int switchChoiceToDayOfWeek(CongestionHistoricalDateChoice choice) {// choice에 따라 다른 요일값으로 설정
        return switch (choice) {
            case MON -> Calendar.MONDAY;
            case TUE -> Calendar.TUESDAY;
            case WED -> Calendar.WEDNESDAY;
            case THU -> Calendar.THURSDAY;
            case FRI -> Calendar.FRIDAY;
            case SAT -> Calendar.SATURDAY;
            case SUN -> Calendar.SUNDAY;
        };
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
