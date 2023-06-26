package bokjak.bokjakserver.domain.category.dto;

import lombok.Getter;

@Getter
public class EnumValue {// Enum -> DTO 변환용 객체. https://jojoldu.tistory.com/122
    private final String key;
    private final String value;

    public EnumValue(EnumModel enumModel) {
        this.key = enumModel.getKey();
        this.value = enumModel.getValue();
    }
}
