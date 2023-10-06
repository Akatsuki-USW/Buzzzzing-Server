package bokjak.bokjakserver.domain.category.dto;

import bokjak.bokjakserver.domain.category.model.LocationCategory;
import bokjak.bokjakserver.domain.category.model.SpotCategory;
import bokjak.bokjakserver.util.enums.EnumQueryValue;
import bokjak.bokjakserver.util.enums.EnumValue;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.*;

import static bokjak.bokjakserver.util.enums.EnumQueryValue.toEnumQueryValues;
import static bokjak.bokjakserver.util.enums.EnumValue.toEnumValues;


public class CategoryDto {
    @Builder
    public record LocationCategoryResponse(
            Long id,
            String name,
            String iconImageUrl
    ) {

        public static LocationCategoryResponse of(LocationCategory locationCategory) {
            return LocationCategoryResponse.builder()
                    .id(locationCategory.getId())
                    .name(locationCategory.getName())
                    .iconImageUrl(locationCategory.getIconImageUrl())
                    .build();
        }
    }

    @Builder
    public record SpotCategoryResponse(
            Long id,
            String name
    ) {

        public static SpotCategoryResponse of(SpotCategory spotCategory) {
            return SpotCategoryResponse.builder()
                    .id(spotCategory.getId())
                    .name(spotCategory.getName())
                    .build();
        }
    }

    @Builder
    public record AllCategories(
            List<LocationCategoryResponse> locationCategories,
            List<SpotCategoryResponse> spotCategories,
            LocalDateTime lastModifiedAt
    ) {

        public static AllCategories of(List<LocationCategoryResponse> locationCategories,
                                       List<SpotCategoryResponse> spotCategories,
                                       LocalDateTime lastModifiedAt) {

            return AllCategories.builder()
                    .locationCategories(locationCategories)
                    .spotCategories(spotCategories)
                    .lastModifiedAt(lastModifiedAt)
                    .build();
        }
    }

    @Builder
    public record AllCategoryResponse(
            List<LocationCategoryResponse> locationCategories,
            List<SpotCategoryResponse> spotCategories,
            List<EnumValue<String>> congestionLevelChoices,
            List<EnumQueryValue<String>> congestionHistoricalDateChoices
    ) {

        public static AllCategoryResponse of(List<LocationCategoryResponse> locationCategories,
                                             List<SpotCategoryResponse> spotCategories) {

            return AllCategoryResponse.builder()
                    .locationCategories(locationCategories)
                    .spotCategories(spotCategories)
                    .congestionLevelChoices(toEnumValues(CongestionLevelChoice.class))
                    .congestionHistoricalDateChoices(toEnumQueryValues(CongestionHistoricalDateChoice.class))
                    .build();
        }

        public static AllCategoryResponse fromAllCategories(AllCategories allCategories) {
            return AllCategoryResponse.builder()
                    .locationCategories(allCategories.locationCategories())
                    .spotCategories(allCategories.spotCategories())
                    .congestionLevelChoices(toEnumValues(CongestionLevelChoice.class))
                    .congestionHistoricalDateChoices(toEnumQueryValues(CongestionHistoricalDateChoice.class))
                    .build();
        }
    }

}
