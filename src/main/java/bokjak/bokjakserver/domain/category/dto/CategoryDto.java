package bokjak.bokjakserver.domain.category.dto;

import bokjak.bokjakserver.domain.category.model.LocationCategory;
import bokjak.bokjakserver.domain.category.model.SpotCategory;
import lombok.Builder;

import java.util.*;
import java.util.stream.Collectors;


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
    public record AllCategoryResponse(
            List<LocationCategoryResponse> locationCategories,
            List<SpotCategoryResponse> spotCategories,
            List<EnumValue> congestionLevelChoices,
            List<EnumDayValue> congestionHistoricalDateChoices
    ) {

        public static AllCategoryResponse of(List<LocationCategoryResponse> locationCategories,
                                             List<SpotCategoryResponse> spotCategories) {

            return AllCategoryResponse.builder()
                    .locationCategories(locationCategories)
                    .spotCategories(spotCategories)
                    .congestionLevelChoices(toEnumValues(CongestionLevelChoice.class))
                    .congestionHistoricalDateChoices(toEnumDayValues(CongestionHistoricalDateChoice.class))
                    .build();
        }
    }

    // enum -> DTO
    private static List<EnumValue> toEnumValues(Class<? extends EnumModel> enumModel) {
        return Arrays
                .stream(enumModel.getEnumConstants())
                .map(EnumValue::new)
                .collect(Collectors.toList());
    }

    private static List<EnumDayValue> toEnumDayValues(Class<? extends EnumModel> enumModel) {
        return Arrays
                .stream(enumModel.getEnumConstants())
                .map(EnumDayValue::new)
                .collect(Collectors.toList());
    }
}
