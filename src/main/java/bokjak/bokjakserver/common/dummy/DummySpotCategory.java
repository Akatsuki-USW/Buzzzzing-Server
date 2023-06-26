package bokjak.bokjakserver.common.dummy;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DummySpotCategory {
    CAFE("cafe"), PLAY("play"), RESTAURANT("restaurant");

    private final String name;
}
