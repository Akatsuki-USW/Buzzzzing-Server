package bokjak.bokjakserver.domain.category.controller;

import bokjak.bokjakserver.common.constant.SwaggerConstants;
import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.domain.category.dto.CategoryDto.AllCategories;
import bokjak.bokjakserver.domain.category.dto.CategoryDto.AllCategoryResponse;
import bokjak.bokjakserver.domain.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = SwaggerConstants.TAG_CATEGORY, description = SwaggerConstants.TAG_CATEGORY_DESCRIPTION)
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = SwaggerConstants.CATEGORY_GET_ALL, description = SwaggerConstants.CATEGORY_GET_ALL_DESCRIPTION)
    public ResponseEntity<ApiResponse<AllCategoryResponse>> getAllCategory(ServletWebRequest request) {
        AllCategories result = categoryService.getAllCategory();
        ZonedDateTime lastModifiedAt = ZonedDateTime.of(result.lastModifiedAt(), ZoneId.systemDefault());

        // Header의 If-Modified-Since와 같다면 304 응답
        if (request.checkNotModified(lastModifiedAt.toInstant().toEpochMilli())) {
            return null;
        }

        return ResponseEntity.ok()
                .cacheControl(CacheControl.noCache())
                .lastModified(lastModifiedAt) // HTTP Cache: Last-Modified
                .body(ApiResponse.success(AllCategoryResponse.fromAllCategories(result)));
    }
}
