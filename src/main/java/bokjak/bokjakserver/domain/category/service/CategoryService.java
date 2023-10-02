package bokjak.bokjakserver.domain.category.service;

import bokjak.bokjakserver.common.dummy.DummyLocationCategory;
import bokjak.bokjakserver.common.dummy.DummySpotCategory;
import bokjak.bokjakserver.common.model.BaseEntity;
import bokjak.bokjakserver.domain.category.dto.CategoryDto.*;
import bokjak.bokjakserver.domain.category.model.LocationCategory;
import bokjak.bokjakserver.domain.category.model.SpotCategory;
import bokjak.bokjakserver.domain.category.repository.LocationCategoryRepository;
import bokjak.bokjakserver.domain.category.repository.SpotCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {
    private final LocationCategoryRepository locationCategoryRepository;
    private final SpotCategoryRepository spotCategoryRepository;

    public AllCategories getAllCategory() {
        List<LocationCategory> locationCategoryList = locationCategoryRepository.findAll();
        List<SpotCategory> spotCategoryList = spotCategoryRepository.findAll();

        LocalDateTime lastUpdatedTime = resolveLastUpdatedTime(locationCategoryList, spotCategoryList);

        List<LocationCategoryResponse> locationCategoryResponses = locationCategoryList
                .stream().map(LocationCategoryResponse::of)
                .toList();
        List<SpotCategoryResponse> spotCategoryResponses = spotCategoryList
                .stream().map(SpotCategoryResponse::of)
                .toList();
        return AllCategories.of(locationCategoryResponses, spotCategoryResponses, lastUpdatedTime);
    }

    private LocalDateTime resolveLastUpdatedTime(List<LocationCategory> locationCategoryList, List<SpotCategory> spotCategoryList) {
        List<LocalDateTime> updatedTimeList = new java.util.ArrayList<>();
        updatedTimeList.addAll(locationCategoryList.stream().map(BaseEntity::getUpdatedAt).toList());
        updatedTimeList.addAll(spotCategoryList.stream().map(BaseEntity::getUpdatedAt).toList());

        return updatedTimeList.stream().max(LocalDateTime::compareTo).orElseThrow(NoSuchElementException::new);
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
