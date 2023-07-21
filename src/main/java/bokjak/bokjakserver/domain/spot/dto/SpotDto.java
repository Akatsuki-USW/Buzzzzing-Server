package bokjak.bokjakserver.domain.spot.dto;

import bokjak.bokjakserver.domain.spot.model.Spot;
import bokjak.bokjakserver.domain.spot.model.SpotImage;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class SpotDto {
    /* Request */

    /* Response */

    @Builder
    public record SpotCardResponse(
            Long id,
            String title,
            String address,
            String thumbnailImageUrl,
            boolean isBookmarked,
            Long spotCategoryId,
            Long userId,
            String userNickname,
            String userProfileImageUrl
    ) {
        public static SpotCardResponse of(
                Spot spot,
                Boolean isBookmarked
        ) {
            return SpotCardResponse.builder()
                    .id(spot.getId())
                    .title(spot.getTitle())
                    .address(spot.getAddress())
                    .thumbnailImageUrl(spot.getSpotImageList().isEmpty() ? null
                            : spot.getSpotImageList().get(0).getImageUrl())
                    .isBookmarked(isBookmarked)
                    .spotCategoryId(spot.getSpotCategory().getId())
                    .userId(spot.getUser().getId())
                    .userNickname(spot.getUser().getNickname())
                    .userProfileImageUrl(spot.getUser().getProfileImageUrl())
                    .build();
        }

    }

    @Builder
    public record SpotDetailResponse(
            Long id,
            String title,
            String address,
            String content,
            List<String> imageUrls,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Long locationId,
            String locationName,
            Long spotCategoryId,
            String spotCategoryName,
            Long userId,
            String userNickname,
            String userProfileImageUrl,
            boolean isBookmarked,
            boolean isAuthor
    ) {
        public static SpotDetailResponse of(
                Spot spot,
                Boolean isBookmarked,
                Boolean isAuthor
        ) {
            return SpotDetailResponse.builder()
                    .id(spot.getId())
                    .title(spot.getTitle())
                    .address(spot.getAddress())
                    .content(spot.getContent())
                    .createdAt(spot.getCreatedAt())
                    .updatedAt(spot.getUpdatedAt())
                    .locationId(spot.getLocation().getId())
                    .locationName(spot.getLocation().getName())
                    .spotCategoryId(spot.getSpotCategory().getId())
                    .spotCategoryName(spot.getSpotCategory().getName())
                    .imageUrls(spot.getSpotImageList().stream().map(SpotImage::getImageUrl).toList())
                    .userId(spot.getUser().getId())
                    .userNickname(spot.getUser().getNickname())
                    .userProfileImageUrl(spot.getUser().getProfileImageUrl())
                    .isBookmarked(isBookmarked)
                    .isAuthor(isAuthor)
                    .build();
        }
    }

    @Builder
    public record BookmarkResponse(
            Long spotId,
            boolean isBookmarked
    ) {
        public static BookmarkResponse of(Long spotId, boolean isBookmarked) {
            return BookmarkResponse.builder()
                    .spotId(spotId)
                    .isBookmarked(isBookmarked)
                    .build();
        }
    }
}
