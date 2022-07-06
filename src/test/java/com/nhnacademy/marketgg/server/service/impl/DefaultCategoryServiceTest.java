package com.nhnacademy.marketgg.server.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.nhnacademy.marketgg.server.dto.CategoryRequest;
import com.nhnacademy.marketgg.server.dto.CategoryResponse;
import com.nhnacademy.marketgg.server.entity.Category;
import com.nhnacademy.marketgg.server.exception.CategoryNotFoundException;
import com.nhnacademy.marketgg.server.repository.CategoryRepository;
import com.nhnacademy.marketgg.server.service.CategoryService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class DefaultCategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 목록 조회")
    void testRetrieveCategories() {
        when(categoryRepository.findAllCategories()).thenReturn(List.of(new CategoryResponse()));

        List<CategoryResponse> responses = categoryService.retrieveCategories();

        assertThat(responses).hasSize(1);
    }

    @DisplayName("카테고리 등록 성공")
    @Test
    void testCreateCategorySuccess() {
        when(categoryRepository.findById(0L))
            .thenReturn(Optional.of(Category.builder()
                                            .categoryNo(0L)
                                            .superCategory(null)
                                            .name("")
                                            .sequence(0)
                                            .code("")
                                            .build()));

        Category category = Category.builder()
                                    .categoryNo(1L)
                                    .superCategory(categoryRepository.findById(0L).orElseThrow())
                                    .name("채소")
                                    .sequence(1)
                                    .code("PROD")
                                    .build();

        when(categoryRepository.findById(1L))
            .thenReturn(Optional.of(category));

        categoryService.createCategory(CategoryRequest.of());

        // REVIEW: 카테고리 레포지토리의 save가 정상적으로 들어가지 않는 것 같습니다. 원인에 대해 알고 싶습니다.
        assertThat(categoryRepository.findById(1L)).isEqualTo(Optional.of(category));
    }

    @DisplayName("카테고리 등록 실패")
    @Test
    void testCreateCategoryFail() {
        // REVIEW: assertThatThrownBy() 에 노란 라인이 뜨면서 Refactor the code of the lambda to have only one invocation possibly throwing a runtime exception.가 뜨는데 원인에 대해 알고 싶습니다.
        assertThatThrownBy(() -> categoryService.createCategory(
            new CategoryRequest(-10L, "", 1, "")))
            .isInstanceOf(CategoryNotFoundException.class);
    }

    @DisplayName("카테고리 수정 성공")
    @Test
    void testUpdateCategorySuccess() {
        when(categoryRepository.findById(0L))
            .thenReturn(Optional.of(Category.builder()
                                            .categoryNo(0L)
                                            .superCategory(null)
                                            .name("")
                                            .sequence(0)
                                            .code("")
                                            .build()));

        Category category = Category.builder()
                                    .categoryNo(1L)
                                    .superCategory(categoryRepository.findById(0L).orElseThrow())
                                    .name("유기농")
                                    .sequence(1)
                                    .code("PROD")
                                    .build();

        when(categoryRepository.findById(1L))
            .thenReturn(Optional.of(category));

        categoryService.updateCategory(1L, CategoryRequest.of());

        assertThat(Objects.requireNonNull(categoryRepository.findById(1L).orElse(null))
                          .getName()).isEqualTo("채소");
    }

    @DisplayName("카테고리 수정 실패 (수정할 카테고리 존재 X)")
    @Test
    void testUpdateCategoryFailWhenNotExistCategory() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.updateCategory(
            -3L, new CategoryRequest(1L, "채소", 1, "PROD")
        )).isInstanceOf(CategoryNotFoundException.class);
    }

    @DisplayName("카테고리 수정 실패 (상위 카테고리 존재 X)")
    @Test
    void testUpdateCategoryFailWhenNotExistSuperCategory() {
        when(categoryRepository.findById(2L))
            .thenReturn(Optional.of(Category.builder()
                                            .categoryNo(2L)
                                            .superCategory(null)
                                            .name("채소")
                                            .sequence(0)
                                            .code("PROD")
                                            .build()));

        assertThatThrownBy(() -> categoryService.updateCategory(
            2L, new CategoryRequest(-1L, "채소", 1, "PROD")
        )).isInstanceOf(CategoryNotFoundException.class);
    }

}
