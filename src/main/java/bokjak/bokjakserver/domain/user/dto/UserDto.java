package bokjak.bokjakserver.domain.user.dto;

import bokjak.bokjakserver.domain.user.model.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public class UserDto {
    public record UserInfoResponse(String email, String nickname, String profileImageUrl) {

        @Builder
        public UserInfoResponse{}

        public static UserInfoResponse of(User user) {
            return UserInfoResponse.builder()
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .profileImageUrl(user.getProfileImageUrl())
                    .build();
        }
    }

    public record UpdateUserInfoRequest(
            @NotBlank
            String email,
            @NotBlank
            String nickname,
            @NotBlank
            String profileImageUrl) {}


    public record HideRequest(
            @NotBlank
            Long blockUserId) {}

    public record HideResponse(boolean blockedResult) {}

    public record NicknameResponse(boolean isAvailableNickname) {}
}
