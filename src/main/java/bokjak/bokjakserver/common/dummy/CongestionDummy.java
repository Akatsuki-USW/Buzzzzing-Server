package bokjak.bokjakserver.common.dummy;

import bokjak.bokjakserver.domain.congestion.model.Congestion;
import bokjak.bokjakserver.domain.congestion.model.DailyCongestionStatistic;
import bokjak.bokjakserver.domain.congestion.model.WeeklyCongestionStatistic;
import bokjak.bokjakserver.domain.congestion.repository.CongestionRepository;
import bokjak.bokjakserver.domain.congestion.repository.DailyCongestionStatisticRepository;
import bokjak.bokjakserver.domain.congestion.repository.WeeklyCongestionStatisticRepository;
import bokjak.bokjakserver.domain.location.model.Location;
import bokjak.bokjakserver.domain.location.repository.LocationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("congestionDummy")
@DependsOn({"userDummy", "locationDummy"})
@RequiredArgsConstructor
@BuzzingDummy
public class CongestionDummy {
    // 개발용. 배포 이후엔 Django 혼잡도 서버에 의존
    private final CongestionRepository congestionRepository;
    private final LocationRepository locationRepository;
    private final DailyCongestionStatisticRepository dailyCongestionStatisticRepository;
    private final WeeklyCongestionStatisticRepository weeklyCongestionStatisticRepository;

    @PostConstruct
    public void init() {
        if (congestionRepository.count() > 0) {
            log.info("[congestionDummy] 혼잡도 데이터가 이미 존재");
        } else {
            createCongestions();
            createCongestionStatistics();
            log.info("[congestionDummy] 혼잡도 더미 생성 완료");
        }
    }

    private void createCongestions() {
        for (Location location : locationRepository.findAll()) {
            for (int k = 1; k <= 3; k++) {
                LocalDateTime now = LocalDateTime.now();

                for (int j = 0; j <= 13; j++) {  // 13일간의 데이터
                    LocalDateTime observedAt = now.minusDays(j).plusHours(k);

                    Congestion congestion = Congestion.builder()
                            .location(location)
                            .congestionLevel((int) (Math.random() * 100) % 3 + 1)// congestion level 랜덤 3가지
                            .observedAt(observedAt)   // 인위적으로 시간 다르게 설정
                            .build();

                    congestionRepository.save(congestion);
                }
            }
        }
    }

    private void createCongestionStatistics() {
        List<Location> allLocations = locationRepository.findAll();
        for (Location location : allLocations) {// 모든 로케이션에 대해
            // daily
            for (int i = 0; i <= 28; i++) {  // 28일간의 데이터
                ArrayList<Map<String, Integer>> list = new ArrayList<>();
                LocalDateTime pastCreatedAt = LocalDateTime.now().minusDays(i + 1);


                for (int j = 9; j <= 24; j++) {// 9~24시
                    list.add(Map.of(
                            "time", j,
                            "congestionLevel", (int) (Math.random() * 100) % 3 + 1
                    ));
                }

                dailyCongestionStatisticRepository.save(
                        DailyCongestionStatistic.builder()
                                .location(location)
                                .content(Map.of("statistics", list))
                                .createdAt(pastCreatedAt)
                                .build()
                );
            }

            // weekly
            for (int i = 0; i < 5; i++) {// 5주간의 데이터
                LocalDateTime pastCreatedAt = LocalDateTime.now().minusWeeks(i + 1);

                weeklyCongestionStatisticRepository.save(
                        WeeklyCongestionStatistic.builder()
                                .location(location)
                                .averageCongestionLevel((float) ((Math.random() * 100) % 3 + 1))
                                .createdAt(pastCreatedAt)
                                .build()
                );
            }
        }
    }
}
