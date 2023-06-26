package bokjak.bokjakserver.domain.category.dto;

import lombok.Getter;

import static bokjak.bokjakserver.domain.category.dto.CongestionHistoricalDateChoice.toDateTime;
import static bokjak.bokjakserver.domain.category.dto.CongestionHistoricalDateChoice.toEnum;

@Getter
public class EnumDayValue {
    private final String key;
    private final String value;
    private final String query;

    public EnumDayValue(EnumModel enumModel) {
        this.key = enumModel.getKey();
        this.value = enumModel.getValue();
        this.query = toDateTime(toEnum(enumModel.getKey()));
    }
}
