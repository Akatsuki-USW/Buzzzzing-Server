package bokjak.bokjakserver.config.jwt;

import io.swagger.v3.oas.annotations.media.Schema;

public record JwtDto (
    @Schema(description = "accessToken", example = "bear eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2dkY3NER0VG9EREs2Q2NLOVFCYXpma0dOTWNNS29pdkJlNGcrbWRCVXgwPSIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE2NzI5ODUwNDYsImV4cCI6MTY3Mjk4ODA0Nn0.IBAJmsKYTQuHGnv4qt14kLY1mTRZK67Xk7iS_P4yGV-mUuiZla84ezgUdpDfdphotFb9tgc-Gzk4wIWXgMZX8w")
    String accessToken,
    @Schema(description = "refreshToken", example = "bear eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2dkY3NER0VG9EREs2Q2NLOVFCYXpma0dOTWNNS29pdkJlNGcrbWRCVXgwPSIsImlhdCI6MTY3Mjk4NTA0NiwiZXhwIjoxNjcyOTkxMDQ2fQ.XrjxsDGAKsD6MSdYHAAt9cGgVLZd7Vlf627YHfgRPLoueOYUV9MV9ZjD6mvWxTeHGa85xGjwOPDCKAxtroySAQ")
    String refreshToken
) {
}
