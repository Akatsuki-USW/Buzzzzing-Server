package bokjak.bokjakserver.util.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class EnumValue <V>{// Enum -> DTO 변환용 객체. https://jojoldu.tistory.com/122
    private final String key;
    private final V value;

    public EnumValue(EnumModel<V> enumModel) {
        this.key = enumModel.getKey();
        this.value = enumModel.getValue();
    }

    // enum -> DTO
    public static List<EnumValue<String>> toEnumValues(Class<? extends EnumModel> enumModelClass) {
        return Arrays
                .stream(enumModelClass.getEnumConstants())
                .map(EnumValue<String>::new)
                .collect(Collectors.toList());
    }
}
