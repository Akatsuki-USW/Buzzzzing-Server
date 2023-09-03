package bokjak.bokjakserver.domain.congestion.dto;

import bokjak.bokjakserver.common.constant.GlobalConstants;
import bokjak.bokjakserver.domain.congestion.model.DailyCongestionStatistic;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

public class CongestionDto {
    @Builder
    public record DailyCongestionStatisticResponse(
            Long id,
            List<DailyCongestionRecord> content
    ) {
        public static DailyCongestionStatisticResponse of(
                DailyCongestionStatistic dailyCongestionStatistic
        ) {
            List<DailyCongestionRecord> records = dailyCongestionStatistic.getContent().get(GlobalConstants.CONTENT_DATA).stream()
                    .filter(it->it.get(GlobalConstants.CONTENT_HOUR) >= GlobalConstants.CONGESTION_STATISTIC_START_TIME) // 9시 이후 데이터만 응답
                    .map(DailyCongestionRecord::of)
                    .toList();

            return DailyCongestionStatisticResponse.builder()
                    .id(dailyCongestionStatistic.getId())
                    .content(records)
                    .build();
        }
    }

    @Builder
    public record DailyCongestionRecord(
            int time,
            int congestionLevel
    ) {
        public static DailyCongestionRecord of(
                Map<String, Integer> raw
        ) {
            return DailyCongestionRecord.builder()
                    .time(raw.get(GlobalConstants.CONTENT_HOUR))
                    .congestionLevel(raw.get(GlobalConstants.CONTENT_CONGESTION_LEVEL))
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
