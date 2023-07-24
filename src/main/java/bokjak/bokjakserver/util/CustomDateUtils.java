package bokjak.bokjakserver.util;

import java.time.*;
import java.util.Calendar;

public class CustomDateUtils {

    public static LocalDateTime makePastWeekDayDateTime(int dayOfWeek, LocalTime localTime) {
        return toSpecificLocalTime(makePastWeekDayDate(dayOfWeek), localTime);
    }

    public static Calendar makePastWeekDayDate(int dayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        // 한 주의 시작 요일 설정: 복쟉복쟉의 일주일 시작 요일은 월요일, 반면 Calendar의 디폴트 시작 요일은 일요일
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        // 일주일 전으로 설정: 과거 일주일간의 혼잡도 데이터를 보기 위함
        calendar.add(Calendar.DATE, -Calendar.DAY_OF_WEEK);
        // 요일에 따라 날짜 설정
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        return calendar;
    }



    private static LocalDateTime toSpecificLocalTime(Calendar calendar, LocalTime localTime) {
        return ZonedDateTime.of(
                LocalDate.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId()),
                localTime,
                ZoneId.systemDefault()
        ).toLocalDateTime();
    }
}
