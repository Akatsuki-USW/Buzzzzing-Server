package bokjak.bokjakserver.domain.congestion.dto;

import bokjak.bokjakserver.common.constant.GlobalConstants;
import bokjak.bokjakserver.domain.congestion.model.DailyCongestionStatistic;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

public class CongestionDto {
    @Builder
    public record DailyCongestionStatisticResponse(
    Long id,
    ArrayList<Map<String, Integer>> content
    ) {
        public static DailyCongestionStatisticResponse of(
                DailyCongestionStatistic dailyCongestionStatistic
        ) {
            return DailyCongestionStatisticResponse.builder()
                    .id(dailyCongestionStatistic.getId())
                    .content(dailyCongestionStatistic.getContent().get(GlobalConstants.CONTENT_DATA))
                    .build();
        }

    }

    @Data
    @NoArgsConstructor
    public static class CongestionPrediction {
        private Integer mayRelaxAt;
        private Integer mayRelaxUntil;
        private Integer mayBuzzAt;
        private Integer mayBuzzUntil;
    }
}
