package bokjak.bokjakserver.common.dummy;

import bokjak.bokjakserver.domain.category.model.SpotCategory;
import bokjak.bokjakserver.domain.category.repository.SpotCategoryRepository;
import bokjak.bokjakserver.domain.location.model.Location;
import bokjak.bokjakserver.domain.location.repository.LocationRepository;
import bokjak.bokjakserver.domain.spot.model.Spot;
import bokjak.bokjakserver.domain.spot.model.SpotImage;
import bokjak.bokjakserver.domain.spot.repository.SpotImageRepository;
import bokjak.bokjakserver.domain.spot.repository.SpotRepository;
import bokjak.bokjakserver.domain.user.model.User;
import bokjak.bokjakserver.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component("spotDummy")
@DependsOn({"userDummy", "locationDummy", "congestionDummy"})
@RequiredArgsConstructor
@Transactional
public class SpotDummy {
    private final LocationRepository locationRepository;
    private final SpotCategoryRepository spotCategoryRepository;
    private final SpotRepository spotRepository;
    private final UserRepository userRepository;
    private final SpotImageRepository spotImageRepository;

    @PostConstruct
    public void init() {
        if (spotRepository.count() > 0) {
            log.info("[5] 스팟 데이터가 이미 존재");
        } else {
            createSpots();
            log.info("[5] 스팟 더미 생성 완료");
        }
        if (spotImageRepository.count() > 0) {
            log.info("[5-1] 스팟 이미지 데이터가 이미 존재");
        } else {
            createSpotImages();
            log.info("[5-1] 스팟 더미 생성 완료");
        }
    }

    private void createSpots() {
        for (Location location : locationRepository.findAll()) {// 모든 로케이션에 대해
            List<SpotCategory> allCategory = spotCategoryRepository.findAll();
            List<User> allUser = userRepository.findAll();

            ArrayList<String> strings = new ArrayList<>();
            strings.add("최고였다!!");
            strings.add("나쁘지 않았다.");
            strings.add("별 거 없었다.");

            for (int i = 0; i < 10; i++) {// 유저 10명
                SpotCategory category = allCategory.get((int) (Math.random() * 100) % 3);
                spotRepository.save(Spot.builder()
                        .location(location)
                        .user(allUser.get(i))
                        .spotCategory(category) // 3가지 중 1 랜덤
                        .address(location.getName() + i + "근처")
                        .title(location.getName() + " 근처 " + category.getName())
                        .content(strings.get((int) (Math.random() * 100) % 3))
                        .build());
            }
        }
    }

    private void createSpotImages() {
        List<Spot> allSpot = spotRepository.findAll();
        ArrayList<String> urls = new ArrayList<>();
        urls.add("https://buz-s3.s3.ap-southeast-2.amazonaws.com/etc/daniel.jpg");
        urls.add("https://buz-s3.s3.ap-southeast-2.amazonaws.com/etc/hani_omg.png");
        urls.add("https://buz-s3.s3.ap-southeast-2.amazonaws.com/etc/hyein.jpg");
        urls.add("https://buz-s3.s3.ap-southeast-2.amazonaws.com/etc/hyerin.png");
        urls.add("https://buz-s3.s3.ap-southeast-2.amazonaws.com/etc/minji.png");

        for (Spot spot : allSpot) {// 스팟마다 0~5개 랜덤으로
            for (int i = 0; i < (int) (Math.random() * 100) % 6; i++) {
                spotImageRepository.save(SpotImage.builder()
                        .spot(spot)
                        .imageUrl(urls.get(i))
                        .build());
            }
        }
    }
}
