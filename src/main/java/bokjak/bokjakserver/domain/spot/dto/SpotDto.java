package bokjak.bokjakserver.domain.spot.dto;

import bokjak.bokjakserver.domain.category.model.SpotCategory;
import bokjak.bokjakserver.domain.location.model.Location;
import bokjak.bokjakserver.domain.spot.model.Spot;
import bokjak.bokjakserver.domain.spot.model.SpotImage;
import bokjak.bokjakserver.domain.user.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

import static bokjak.bokjakserver.common.constant.ConstraintConstants.*;

public class SpotDto {

    /* Request */
    @Builder
    public record CreateSpotRequest(
            @NotBlank @Size(max = SPOT_TITLE_MAX_LENGTH)
            String title,
            @NotNull @Size(max = SPOT_ADDRESS_MAX_LENGTH)
            String address,
            @NotBlank @Size(max = SPOT_CONTENT_MAX_LENGTH)
            String content,
            @NotNull @Size(max = SPOT_IMAGE_MAX_SIZE)
            List<String> imageUrls
    ) {
        public Spot toEntity(User user, Location location, SpotCategory spotCategory) {
            return Spot.builder()
                    .title(title)
                    .address(address)
                    .content(content)
                    .user(user)
                    .location(location)
                    .spotCategory(spotCategory)
                    .build();
        }
    }

    /* Response */
    @Builder
    public record SpotCardResponse(
            Long id,
            String title,
            String address,
            String thumbnailImageUrl,
            Long userId,
            String userNickname,
            String userProfileImageUrl,
            boolean isBookmarked
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
                    .userId(spot.getUser().getId())
                    .userNickname(spot.getUser().getNickname())
                    .userProfileImageUrl(spot.getUser().getProfileImageUrl())
                    .isBookmarked(isBookmarked)
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

    @Builder
    public record SpotIdResponse(
            Long spotId
    ) {
        public static SpotIdResponse of(Spot spot) {
            return SpotIdResponse.builder()
                    .spotId(spot.getId())
                    .build();
        }
    }
}
