package bokjak.bokjakserver.domain.ban.dto;

import bokjak.bokjakserver.domain.ban.model.Ban;
import lombok.Builder;

import java.util.List;

import static bokjak.bokjakserver.util.CustomDateUtils.customDateFormatOnlyDate;

public class BanDto {

    public record BanResponse(
            String title,
            String content,
            String banStartAt,
            String banEndedAt
    ) {
        @Builder
        public BanResponse {}

        public static BanResponse of (Ban ban) {
            return BanResponse.builder()
                    .title(ban.getTitle())
                    .content(ban.getContent())
                    .banStartAt(customDateFormatOnlyDate(ban.getBanStartedAt()))
                    .banEndedAt(customDateFormatOnlyDate(ban.getBanEndedAt()))
                    .build();
        }
    }

    public record BanListResponse(
            List<BanResponse> banList
    ) {
        @Builder
        public BanListResponse {}

        public static BanListResponse of(List<BanResponse> banList) {
            return BanListResponse.builder()
                    .banList(banList)
                    .build();
        }
    }
}
