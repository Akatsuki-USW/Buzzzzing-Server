package bokjak.bokjakserver.util.enums;

import bokjak.bokjakserver.domain.category.dto.CongestionHistoricalDateChoice;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static bokjak.bokjakserver.domain.category.dto.CongestionHistoricalDateChoice.toDateTime;

@Getter
public class EnumQueryValue<V>{
    private final String key;
    private final V value;
    private final String query;

    public EnumQueryValue(EnumModel<V> enumModel) {
        this.key = enumModel.getKey();
        this.value = enumModel.getValue();
        this.query = toDateTime(CongestionHistoricalDateChoice.toEnum(enumModel.getKey()));
    }

    public static List<EnumQueryValue<String>> toEnumQueryValues(Class<? extends EnumModel<String>> enumModel) {
        return Arrays
                .stream(enumModel.getEnumConstants())
                .map(EnumQueryValue::new)
                .collect(Collectors.toList());
    }
}
