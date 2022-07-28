package com.nhnacademy.marketgg.server.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.marketgg.server.dto.request.DefaultPageRequest;
import com.nhnacademy.marketgg.server.dto.request.ReviewCreateRequest;
import com.nhnacademy.marketgg.server.dto.response.common.SingleResponse;
import com.nhnacademy.marketgg.server.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ReviewService reviewService;

    private ReviewCreateRequest reviewRequest;

    @BeforeEach
    void setUp() {
        reviewRequest = new ReviewCreateRequest();
    }

    @Test
    @DisplayName("일반 리뷰 등록 테스트")
    void testCreateReview() throws Exception {
        String content = objectMapper.writeValueAsString(reviewRequest);

        this.mockMvc.perform(post("/products/{productId}/review/{memberUuid}", 1L, "admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isCreated());

        then(reviewService).should().createReview(any(ReviewCreateRequest.class), anyString());
    }

    @Test
    @DisplayName("후기 전체 조회 테스트")
    void testRetrieveReviews() throws Exception {
        given(reviewService.retrieveReviews(new DefaultPageRequest().getPageable())).willReturn(new SingleResponse<>());

        this.mockMvc.perform(get("/products/{productId}/review/", 1L))
            .andExpect(status().isOk());

        then(reviewService).should().retrieveReviews(any(Pageable.class));
    }
}