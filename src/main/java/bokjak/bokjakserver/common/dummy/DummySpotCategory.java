package bokjak.bokjakserver.common.dummy;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DummySpotCategory {
    CAFE("카페"), PLAY("놀거리"), RESTAURANT("맛집");

    private final String name;
}
