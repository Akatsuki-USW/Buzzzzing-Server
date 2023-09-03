package bokjak.bokjakserver.domain.congestion.model;

import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.domain.congestion.exception.CongestionException;
import bokjak.bokjakserver.util.enums.EnumModel;
import bokjak.bokjakserver.util.queries.SortOrder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CongestionLevel implements EnumModel<Integer> {
    RELAX(1), NORMAL(2), BUZZING(3);

    private final int value;

    @Override
    public String getKey() {
        return name();
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public static CongestionLevel toEnum(String stringParam) {
        if (stringParam == null) return null;
        else return switch (stringParam.toUpperCase()) {
            case "RELAX" -> RELAX;
            case "NORMAL" -> NORMAL;
            case "BUZZING" -> BUZZING;

            default -> throw new CongestionException(StatusCode.CHOICE_NOT_EXIST);
        };
    }

    public static CongestionLevel toEnum(Integer intParam) {
        if (intParam == null) return null;

        return switch (intParam) {
            case 1 -> RELAX;
            case 2 -> NORMAL;
            case 3 -> BUZZING;

            default -> throw new CongestionException(StatusCode.CHOICE_NOT_EXIST);
        };
    }

    // ASC DESC로 변환
    public static SortOrder toSortOrder(CongestionLevel congestionLevel) {
        if (congestionLevel == null) return null;

        return switch (congestionLevel) {
            case RELAX -> SortOrder.ASC;
            case BUZZING -> SortOrder.DESC;
            case NORMAL -> null;
        };
    }
}
