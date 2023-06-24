package bokjak.bokjakserver.domain.congestion.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CongestionLevel {
    RELAX(1), NORMAL(2), BUZZ(3), VERY_BUZZ(4);

    private final int value;
}
