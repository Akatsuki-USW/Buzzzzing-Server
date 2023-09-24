package bokjak.bokjakserver.common.dummy;

import bokjak.bokjakserver.domain.category.model.LocationCategory;
import bokjak.bokjakserver.domain.category.repository.LocationCategoryRepository;
import bokjak.bokjakserver.domain.location.model.Location;
import bokjak.bokjakserver.domain.location.repository.LocationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component("locationDummy")
@DependsOn("categoryDummy")
@RequiredArgsConstructor
@BuzzingDummy
public class LocationDummy {
    private final LocationCategoryRepository locationCategoryRepository;
    private final LocationRepository locationRepository;

    @PostConstruct
    public void init() {
        if (locationRepository.count() > 0) {
            log.info("[locationDummy] 로케이션 데이터가 이미 존재");
        } else {
            createLocations();
            log.info("[locationDummy] 로케이션 더미 생성 완료");
        }
    }

    private void createLocations() {
        List<LocationCategory> allCategory = locationCategoryRepository.findAll();

        locationRepository.save(Location.builder().name("서울 암사동 유적").locationCategory(allCategory.get(3)).realtimeCongestionLevel(1).build());
        locationRepository.save(Location.builder().name("창덕궁종묘").locationCategory(allCategory.get(3)).realtimeCongestionLevel(3).build());
        locationRepository.save(Location.builder().name("가산디지털단지역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(1).build());
        locationRepository.save(Location.builder().name("강남역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(2).build());
        locationRepository.save(Location.builder().name("건대입구역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(2).build());
        locationRepository.save(Location.builder().name("고덕역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(1).build());
        locationRepository.save(Location.builder().name("고속터미널역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(1).build());
        locationRepository.save(Location.builder().name("교대역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(1).build());
        locationRepository.save(Location.builder().name("구로디지털단지역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(1).build());
        locationRepository.save(Location.builder().name("구로역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(2).build());
        locationRepository.save(Location.builder().name("군자역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(3).build());
        locationRepository.save(Location.builder().name("남구로역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(1).build());
        locationRepository.save(Location.builder().name("대림역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(2).build());
        locationRepository.save(Location.builder().name("동대문역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(3).build());
        locationRepository.save(Location.builder().name("뚝섬역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(2).build());
        locationRepository.save(Location.builder().name("미아사거리역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(2).build());
        locationRepository.save(Location.builder().name("발산역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(2).build());
        locationRepository.save(Location.builder().name("북한산우이역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(3).build());
        locationRepository.save(Location.builder().name("사당역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(2).build());
        locationRepository.save(Location.builder().name("삼각지역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(2).build());
        locationRepository.save(Location.builder().name("서울대입구역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(3).build());
        locationRepository.save(Location.builder().name("선릉역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(2).build());
        locationRepository.save(Location.builder().name("성신여대입구역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(2).build());
        locationRepository.save(Location.builder().name("수유역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(1).build());
        locationRepository.save(Location.builder().name("신논현역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(1).build());
        locationRepository.save(Location.builder().name("신도림역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(1).build());
        locationRepository.save(Location.builder().name("신림역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(1).build());
        locationRepository.save(Location.builder().name("신촌이대역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(1).build());
        locationRepository.save(Location.builder().name("양재역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(2).build());
        locationRepository.save(Location.builder().name("역삼역").locationCategory(allCategory.get(0)).realtimeCongestionLevel(2).build());
        locationRepository.save(Location.builder().name("강서한강공원").locationCategory(allCategory.get(4)).realtimeCongestionLevel(2).build());
        locationRepository.save(Location.builder().name("고척돔").locationCategory(allCategory.get(6)).realtimeCongestionLevel(1).build());
        locationRepository.save(Location.builder().name("광나루한강공원").locationCategory(allCategory.get(4)).realtimeCongestionLevel(1).build());
        locationRepository.save(Location.builder().name("난지한강공원").locationCategory(allCategory.get(4)).realtimeCongestionLevel(1).build());
        locationRepository.save(Location.builder().name("노들섬").locationCategory(allCategory.get(6)).realtimeCongestionLevel(3).build());
        locationRepository.save(Location.builder().name("뚝섬한강공원").locationCategory(allCategory.get(4)).realtimeCongestionLevel(1).build());
        locationRepository.save(Location.builder().name("망원한강공원").locationCategory(allCategory.get(4)).realtimeCongestionLevel(1).build());
        locationRepository.save(Location.builder().name("불광천").locationCategory(allCategory.get(6)).realtimeCongestionLevel(1).build());
        locationRepository.save(Location.builder().name("서울광장").locationCategory(allCategory.get(4)).realtimeCongestionLevel(1).build());
        locationRepository.save(Location.builder().name("서울대공원").locationCategory(allCategory.get(4)).realtimeCongestionLevel(3).build());
    }
}