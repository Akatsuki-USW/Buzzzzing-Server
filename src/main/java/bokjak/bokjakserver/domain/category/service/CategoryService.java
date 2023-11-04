package bokjak.bokjakserver.domain.category.service;

import bokjak.bokjakserver.common.dummy.DummyLocationCategory;
import bokjak.bokjakserver.common.dummy.DummySpotCategory;
import bokjak.bokjakserver.domain.category.dto.CategoryDto.*;
import bokjak.bokjakserver.domain.category.model.LocationCategory;
import bokjak.bokjakserver.domain.category.model.SpotCategory;
import bokjak.bokjakserver.domain.category.repository.LocationCategoryRepository;
import bokjak.bokjakserver.domain.category.repository.SpotCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final LocationCategoryRepository locationCategoryRepository;
    private final SpotCategoryRepository spotCategoryRepository;

    public AllCategoryResponse getAllCategory(){
        List<LocationCategoryResponse> locationCategoryResponses = locationCategoryRepository.findAll()
                .stream().map(LocationCategoryResponse::of)
                .toList();
        List<SpotCategoryResponse> spotCategoryResponses = spotCategoryRepository.findAll()
                .stream().map(SpotCategoryResponse::of)
                .toList();
        return AllCategoryResponse.of(locationCategoryResponses, spotCategoryResponses);
    }

    // Dummy 생성 및 테스트용 메서드
    @Transactional
    public void createDummyLocationCategory(DummyLocationCategory dummyLocationCategory) {
        LocationCategory category = LocationCategory.builder()
                .name(dummyLocationCategory.getName())
                .iconImageUrl(dummyLocationCategory.getIconImageUrl())
                .build();
        locationCategoryRepository.save(category);
    }

    @Transactional
    public void createDummySpotCategory(DummySpotCategory dummySpotCategory) {
        SpotCategory category = SpotCategory.builder()
                .name(dummySpotCategory.getName())
                .build();
        spotCategoryRepository.save(category);
    }
}
