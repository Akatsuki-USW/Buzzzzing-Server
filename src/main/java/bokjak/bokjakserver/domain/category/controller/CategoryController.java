package bokjak.bokjakserver.domain.category.controller;

import bokjak.bokjakserver.common.constant.SwaggerConstants;
import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.domain.category.dto.CategoryDto.AllCategoryResponse;
import bokjak.bokjakserver.domain.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = SwaggerConstants.TAG_CATEGORY, description = SwaggerConstants.TAG_CATEGORY_DESCRIPTION)
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = SwaggerConstants.CATEGORY_GET_ALL, description = SwaggerConstants.CATEGORY_GET_ALL_DESCRIPTION)
    public ApiResponse<AllCategoryResponse> getAllCategory() {
        return ApiResponse.success(categoryService.getAllCategory());
    }
}
