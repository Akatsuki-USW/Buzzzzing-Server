package bokjak.bokjakserver.domain.ban.dto;

import bokjak.bokjakserver.common.constant.GlobalConstants;
import bokjak.bokjakserver.domain.ban.model.Ban;
import lombok.Builder;

import java.util.List;

import static bokjak.bokjakserver.util.CustomDateUtils.customDateFormat;

public class BanDto {

    public record BanResponse(
            String title,
            String content,
            String banStartAt,
            String banEndedAt
    ) {
        @Builder
        public BanResponse {
        }

        public static BanResponse of(Ban ban) {
            return BanResponse.builder()
                    .title(ban.getTitle())
                    .content(ban.getContent())
                    .banStartAt(customDateFormat(ban.getBanStartedAt(), GlobalConstants.DATE_FORMAT_YYYY_MM_DD))
                    .banEndedAt(customDateFormat(ban.getBanEndedAt(), GlobalConstants.DATE_FORMAT_YYYY_MM_DD))
                    .build();
        }
    }

    public record BanListResponse(
            List<BanResponse> banList
    ) {
        @Builder
        public BanListResponse {
        }

        public static BanListResponse of(List<BanResponse> banList) {
            return BanListResponse.builder()
                    .banList(banList)
                    .build();
        }
    }
}
