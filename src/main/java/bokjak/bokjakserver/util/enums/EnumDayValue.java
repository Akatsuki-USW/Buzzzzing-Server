package bokjak.bokjakserver.util.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static bokjak.bokjakserver.domain.category.dto.CongestionHistoricalDateChoice.toDateTime;
import static bokjak.bokjakserver.domain.category.dto.CongestionHistoricalDateChoice.toEnum;

@Getter
public class EnumDayValue <V>{
    private final String key;
    private final V value;
    private final String query;

    public EnumDayValue(EnumModel<V> enumModel) {
        this.key = enumModel.getKey();
        this.value = enumModel.getValue();
        this.query = toDateTime(toEnum(enumModel.getKey()));
    }

    public static List<EnumDayValue> toEnumDayValues(Class<? extends EnumModel> enumModel) {
        return Arrays
                .stream(enumModel.getEnumConstants())
                .map(EnumDayValue::new)
                .collect(Collectors.toList());
    }
}
