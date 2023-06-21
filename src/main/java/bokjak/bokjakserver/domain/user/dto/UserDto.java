package bokjak.bokjakserver.domain.user.dto;

public class UserDto {
    public record UserInfoResponse(String email, String nickname, String profileImageUrl) {}


    public record HideRequest(Long blockUserId) {}

    public record HideResponse(boolean blockedResult) {}

    public record NicknameResponse(boolean isAvailableNickname) {}
}
