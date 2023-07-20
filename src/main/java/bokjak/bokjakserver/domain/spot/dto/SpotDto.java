package bokjak.bokjakserver.domain.spot.dto;

import bokjak.bokjakserver.domain.spot.model.Spot;
import lombok.Builder;

public class SpotDto {
    /* Request */

    /* Response */

    @Builder
    public record SpotCardResponse(
            Long id,
            String title,
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
}
