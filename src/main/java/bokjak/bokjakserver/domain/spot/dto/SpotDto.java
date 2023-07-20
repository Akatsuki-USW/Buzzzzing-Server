package bokjak.bokjakserver.domain.spot.dto;

import bokjak.bokjakserver.domain.spot.model.Spot;
import lombok.Builder;

import java.net.URL;

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
            String userProfileImageUrl    // TODO ?? 이거 DB에서 나올 때 뭘로 나오려나
    ) {
        public static SpotCardResponse of(
                Spot spot,
                Boolean isBookmarked
        ) {
            return SpotCardResponse.builder()
                    .id(spot.getId())
                    .title(spot.getTitle())
                    .thumbnailImageUrl(spot.getThumbnailImageUrl())
                    .isBookmarked(isBookmarked)
                    .spotCategoryId(spot.getSpotCategory().getId())
                    .userId(spot.getUser().getId())
                    .userNickname(spot.getUser().getNickname())
                    .userProfileImageUrl(spot.getUser().getProfileImageUrl())
                    .build();
        }

    }
}
