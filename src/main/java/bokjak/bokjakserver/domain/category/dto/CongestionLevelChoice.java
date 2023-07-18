package bokjak.bokjakserver.domain.category.dto;

import bokjak.bokjakserver.util.enums.EnumModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CongestionLevelChoice implements EnumModel<String> {
    RELAX("여유순"), NORMAL("보통"), BUZZING("복쟉순");

    private final String value;

    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return value;
    }
}
