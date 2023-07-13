package bokjak.bokjakserver.util.queries;

import bokjak.bokjakserver.util.enums.EnumModel;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SortOrder implements EnumModel<String> {
    ASC("ASC"), DESC("DESC");

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
