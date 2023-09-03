package bokjak.bokjakserver.domain.location;

import bokjak.bokjakserver.domain.category.model.LocationCategory;
import bokjak.bokjakserver.domain.location.model.Location;

public class LocationMockUtils {
    public static Location makeDummyLocation() {
        LocationCategory category = LocationCategory.builder()
                .id(2L)
                .name("놀이공원")
                .iconImageUrl("https://s3-buz.s3.ap-northeast-2.amazonaws.com/constant/amusement.png")
                .build();

        return Location.builder()
                .name("망원한강공원")
                .id(83L)
                .apiId(94)
                .locationCategory(category)
                .build();
    }
}
