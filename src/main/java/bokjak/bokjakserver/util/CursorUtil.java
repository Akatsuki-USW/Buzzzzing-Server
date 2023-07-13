package bokjak.bokjakserver.util;

public class CursorUtil {
    public static final int TEN = 10;
    public static final int TWENTY = 20;

    public static Long getNextCursorId(Long currentCursorId, int amount, Long totalElements) {
        if (currentCursorId == null) {
            return (long) amount;
        }
        long nextCursorId = currentCursorId + amount;
        if (totalElements < nextCursorId) {
            return null;
        }
        return nextCursorId;
    }
}
