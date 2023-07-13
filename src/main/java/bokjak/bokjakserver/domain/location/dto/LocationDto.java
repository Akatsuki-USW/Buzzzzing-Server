package bokjak.bokjakserver.domain.location.dto;

import bokjak.bokjakserver.domain.congestion.dto.CongestionDto.CongestionPrediction;
import bokjak.bokjakserver.domain.congestion.model.Congestion;
import bokjak.bokjakserver.domain.congestion.model.CongestionLevel;
import bokjak.bokjakserver.domain.location.model.Location;
import lombok.Builder;

import java.time.LocalDateTime;

public class LocationDto {
    /**
     * Response
     */
    @Builder
    public record LocationCardResponse(
            Long id,
            String name,
            Long categoryId,
            String categoryName,
            String categoryIconUrl,
            boolean isBookmarked,
            CongestionLevel congestionSymbol,
            int congestionLevel,
            int bookMarkCount

    ) {
        public static LocationCardResponse of(
                Location location,
                boolean isBookmarked
        ) {
            return LocationCardResponse.builder()
                    .id(location.getId())
                    .name(location.getName())
                    .categoryId(location.getLocationCategory().getId())
                    .categoryName(location.getLocationCategory().getName())
                    .categoryIconUrl(location.getLocationCategory().getIconImageUrl())
                    .congestionSymbol(CongestionLevel.toEnum(location.getCongestionList().get(0).getCongestionLevel()))
                    .congestionLevel(location.getCongestionList().get(0).getCongestionLevel())
                    .bookMarkCount(location.getLocationBookmarkList().size())
                    .isBookmarked(isBookmarked)
                    .build();
        }
    }

    @Builder
    public record LocationDetailResponse(
            Long id,
            String name,
            Long categoryId,
            String categoryName,
            int bookMarkCount,
            boolean isBookmarked,
            CongestionLevel congestionSymbol,
            int congestionLevel,
            Long congestionId,
            LocalDateTime observedAt,
            Integer mayRelaxAt,
            Integer mayRelaxUntil,
            Integer mayBuzzAt,
            Integer mayBuzzUntil
    ) {
        public static LocationDetailResponse of(
                Location location,
                Congestion congestion,
                CongestionPrediction congestionPrediction,
                boolean isBookmarked
        ) {
            return LocationDetailResponse.builder()
                    .id(location.getId())
                    .name(location.getName())
                    .categoryId(location.getLocationCategory().getId())
                    .categoryName(location.getLocationCategory().getName())
                    .bookMarkCount(location.getLocationBookmarkList().size())
                    .isBookmarked(isBookmarked)
                    .congestionSymbol(CongestionLevel.toEnum(congestion.getCongestionLevel()))
                    .congestionLevel(congestion.getCongestionLevel())
                    .congestionId(congestion.getId())
                    .observedAt(congestion.getObservedAt())
                    .mayRelaxAt(congestionPrediction.getMayRelaxAt())
                    .mayRelaxUntil(congestionPrediction.getMayRelaxUntil())
                    .mayBuzzAt(congestionPrediction.getMayBuzzAt())
                    .mayBuzzUntil(congestionPrediction.getMayBuzzUntil())
                    .build();
        }
    }

    @Builder
    public record BookmarkResponse(
            Long locationId,
            boolean isBookmarked
    ) {
        public static BookmarkResponse of(Long locationId, boolean isBookmarked) {
            return BookmarkResponse.builder()
                    .locationId(locationId)
                    .isBookmarked(isBookmarked)
                    .build();
        }
    }
}
