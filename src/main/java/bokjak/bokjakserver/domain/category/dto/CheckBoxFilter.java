package bokjak.bokjakserver.domain.category.dto;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class CheckBoxFilter {   // TODO: refactor
    public static final List<Map<String, String>> locationRelaxFilter = List.of(
            Map.of("name", "CONGESTED", "displayName", "인기순"),
            Map.of("name", "RELAXED", "displayName", "여유순"),
            Map.of("name", "NORMAL", "displayName", "보통")
    );

    public static final List<Map<String, String>> congestionDateFilter = List.of(
            Map.of("name", "TODAY", "displayName", "Last week, today"),
            Map.of("name", "MON", "displayName", "Last Mon"),
            Map.of("name", "TUE", "displayName", "Last Tue"),
            Map.of("name", "WED", "displayName", "Last Wed"),
            Map.of("name", "THU", "displayName", "Last Thu"),
            Map.of("name", "FRI", "displayName", "Last Fri"),
            Map.of("name", "SAT", "displayName", "Last Sat"),
            Map.of("name", "SUN", "displayName", "Last Sun")
    );

    // toDateTime: congestionDateFilter에 대해 요청된 요일(name 필드)에 따라 값을 다르게 변환. SQL 날짜 형식에 맞도록 포맷
    public static String toDateTime(String stringParam) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);    // 일주일 전

        switch (stringParam.toUpperCase()) {
            case "TODAY" -> {
                break;
            }
            case "MON" -> calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            case "TUE" -> calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
            case "WED" -> calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
            case "THU" -> calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
            case "FRI" -> calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
            case "SAT" -> calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            case "SUN" -> calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

            // TODO: 예외처리를 응답으로 하는 방법 알아보기
            default -> throw new IllegalArgumentException("Unexpected value: " + stringParam.toUpperCase());
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(calendar.getTime());
    }
}
