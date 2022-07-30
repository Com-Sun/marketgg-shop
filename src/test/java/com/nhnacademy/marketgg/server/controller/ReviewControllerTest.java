package com.nhnacademy.marketgg.server.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.marketgg.server.dto.request.DefaultPageRequest;
import com.nhnacademy.marketgg.server.dto.request.ReviewCreateRequest;
import com.nhnacademy.marketgg.server.dto.request.ReviewUpdateRequest;
import com.nhnacademy.marketgg.server.dto.response.common.SingleResponse;
import com.nhnacademy.marketgg.server.entity.Asset;
import com.nhnacademy.marketgg.server.service.ReviewService;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ReviewService reviewService;

    private ReviewCreateRequest reviewRequest;
    private ReviewUpdateRequest reviewUpdateRequest;

    @BeforeEach
    void setUp() {
        reviewRequest = new ReviewCreateRequest();
        reviewUpdateRequest = new ReviewUpdateRequest();
    }

    @Test
    @DisplayName("사진 리뷰 등록 테스트")
    void testCreateReview() throws Exception {
        String content = objectMapper.writeValueAsString(reviewRequest);

        URL url = getClass().getClassLoader().getResource("lee.png");
        String filePath = Objects.requireNonNull(url).getPath();

        MockMultipartFile file =
            new MockMultipartFile("images", "lee.png", "image/png", new FileInputStream(filePath));

        MockMultipartFile dto = new MockMultipartFile("reviewRequest", "jsondata", "application/json",
                                                      content.getBytes(StandardCharsets.UTF_8));

        this.mockMvc.perform(multipart("/products/{productId}/review/{memberUuid}", 1L, "admin")
                                 .file(dto)
                                 .file(file)
                                 .file(file)
                                 .contentType(MediaType.APPLICATION_JSON_VALUE)
                                 .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                 .content(content))
                    .andExpect(status().isCreated());

        then(reviewService).should().createReview(any(ReviewCreateRequest.class), any(List.class) , anyString());
    }

    @Test
    @DisplayName("후기 전체 조회 테스트")
    void testRetrieveReviews() throws Exception {
        given(reviewService.retrieveReviews(new DefaultPageRequest().getPageable())).willReturn(new SingleResponse<>());

        this.mockMvc.perform(get("/products/{productId}/review/", 1L))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        then(reviewService).should().retrieveReviews(any(Pageable.class));
    }

    @Test
    @DisplayName("후기 상세 조회 테스트")
    void testRetrieveReviewDetails() throws Exception {
        given(reviewService.retrieveReviewDetails(anyLong())).willReturn(new SingleResponse<>());

        this.mockMvc.perform(get("/products/{productId}/review/{reviewId}", 1L, 1L))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        then(reviewService).should().retrieveReviewDetails(anyLong());
    }

    @Test
    @DisplayName("후기 수정 테스트")
    void testUpdateReview() throws Exception {
        String content = objectMapper.writeValueAsString(reviewUpdateRequest);
        BDDMockito.willDoNothing().given(reviewService)
                  .updateReview(any(ReviewUpdateRequest.class), anyLong());

        this.mockMvc.perform(put("/products/{productId}/review/{reviewId}", 1L, 1L)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(content))
                    .andExpect(status().isOk());

        then(reviewService).should().updateReview(any(ReviewUpdateRequest.class), anyLong());
    }

    @Test
    @DisplayName("후기 삭제 테스트")
    void testDeleteReview() throws Exception {
        this.mockMvc.perform(delete("/products/{productId}/review/{reviewId}", 1L, 1L))
                    .andExpect(status().isOk());

        then(reviewService).should().deleteReview(anyLong());
    }
}