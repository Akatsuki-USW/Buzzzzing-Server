package bokjak.bokjakserver.common.dummy;

import bokjak.bokjakserver.domain.category.repository.LocationCategoryRepository;
import bokjak.bokjakserver.domain.category.service.CategoryService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static bokjak.bokjakserver.common.dummy.DummyLocationCategory.*;
import static bokjak.bokjakserver.common.dummy.DummySpotCategory.*;

@Slf4j
@RequiredArgsConstructor
@Component("categoryDummy")
@BuzzingDummy
public class CategoryDummy {
    private final CategoryService categoryService;
    private final LocationCategoryRepository locationCategoryRepository;

    @PostConstruct
    public void init() {
        if (locationCategoryRepository.count() > 0) {
            log.info("[categoryDummy] 카테고리가 이미 존재");
            return;
        }

        // 로케이션 카테고리
        categoryService.createDummyLocationCategory(SUBWAY);
        categoryService.createDummyLocationCategory(AMUSE);
        categoryService.createDummyLocationCategory(DEPART);
        categoryService.createDummyLocationCategory(ETC);
        categoryService.createDummyLocationCategory(PARK);
        categoryService.createDummyLocationCategory(MART);
        categoryService.createDummyLocationCategory(VACATION);
        categoryService.createDummyLocationCategory(MARKET);

        // 스팟 카테고리
        categoryService.createDummySpotCategory(CAFE);
        categoryService.createDummySpotCategory(PLAY);
        categoryService.createDummySpotCategory(RESTAURANT);

        log.info("[categoryDummy] 카테고리 더미 생성 완료");
    }
}
