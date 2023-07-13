package bokjak.bokjakserver.domain.congestion.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CongestionLevel implements EnumModel<Integer> {
    RELAX(1), NORMAL(2), BUZZING(3);

    private final int value;
}
