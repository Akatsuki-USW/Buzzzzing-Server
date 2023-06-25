package bokjak.bokjakserver.domain.category.controller;

import bokjak.bokjakserver.common.dto.ApiResponse;
import bokjak.bokjakserver.domain.category.dto.CategoryDto.*;
import bokjak.bokjakserver.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ApiResponse<AllCategoryResponse> getAllCategory(){
        return ApiResponse.success(categoryService.getAllCategory());
    }
}
