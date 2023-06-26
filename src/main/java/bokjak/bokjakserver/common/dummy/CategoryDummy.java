package bokjak.bokjakserver.common.dummy;

import bokjak.bokjakserver.domain.category.service.CategoryService;
import bokjak.bokjakserver.domain.category.repository.LocationCategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component("categoryDummy")
@RequiredArgsConstructor
@Transactional
public class CategoryDummy {
    private final CategoryService categoryService;
    private final LocationCategoryRepository locationCategoryRepository;

    @PostConstruct
    public void init(){
        if (locationCategoryRepository.count() > 0) {
            log.info("[1] 카테고리가 이미 존재");
            return;
        }

        // TODO: 추후 S3 이미지 오브젝트 넣고 url 업데이트
        // 로케이션 카테고리
        categoryService.createLocationCategory("지하철", "");
        categoryService.createLocationCategory("놀이공원", "");
        categoryService.createLocationCategory("백화점", "");
        categoryService.createLocationCategory("기타", "");
        categoryService.createLocationCategory("공원", "");
        categoryService.createLocationCategory("마트", "");
        categoryService.createLocationCategory("휴양지", "");
        categoryService.createLocationCategory("시장", "");
        categoryService.createLocationCategory("종합", "");

        // 스팟 카테고리
        categoryService.createSpotCategory("cafe");
        categoryService.createSpotCategory("play");
        categoryService.createSpotCategory("restaurant");

        log.info("[1] 카테고리 더미 생성 완료");
    }
}
