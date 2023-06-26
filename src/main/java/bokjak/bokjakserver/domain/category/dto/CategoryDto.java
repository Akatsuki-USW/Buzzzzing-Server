package bokjak.bokjakserver.domain.category.dto;

import bokjak.bokjakserver.domain.category.model.LocationCategory;
import bokjak.bokjakserver.domain.category.model.SpotCategory;
import lombok.Builder;

import java.util.*;


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
            List<Map<String, String>> locationRelaxFilter,
            List<Map<String, String>> congestionDateFilter
    ) {

        public static AllCategoryResponse of(List<LocationCategoryResponse> locationCategories,
                                             List<SpotCategoryResponse> spotCategories) {

            return AllCategoryResponse.builder()
                    .locationCategories(locationCategories)
                    .spotCategories(spotCategories)
                    .locationRelaxFilter(CheckBoxFilter.locationRelaxFilter)
                    .congestionDateFilter(CheckBoxFilter.congestionDateFilter)
                    .build();
        }
    }
}
