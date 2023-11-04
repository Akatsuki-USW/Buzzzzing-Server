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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.Duration;
import java.time.LocalDateTime;
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
        AllCategories allCategories = categoryService.getAllCategory();
        LocalDateTime lastModifiedAt = allCategories.lastModifiedAt();

        // Last-Modified 검증. 같다면 304 응답
        if (request.checkNotModified(String.valueOf(lastModifiedAt))) {
            return null;
        }

        return ResponseEntity.ok()
                .cacheControl(CacheControl.noCache())
                .lastModified(ZonedDateTime.of(lastModifiedAt, ZoneId.systemDefault()))
                .body(ApiResponse.success(AllCategoryResponse.fromAllCategories(allCategories)));
    }
}
