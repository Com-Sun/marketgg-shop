package com.nhnacademy.marketgg.server.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.nhnacademy.marketgg.server.dto.request.MemberCreateRequest;
import com.nhnacademy.marketgg.server.dto.request.ReviewCreateRequest;
import com.nhnacademy.marketgg.server.dto.response.ReviewResponse;
import com.nhnacademy.marketgg.server.dto.response.common.SingleResponse;
import com.nhnacademy.marketgg.server.entity.Asset;
import com.nhnacademy.marketgg.server.entity.Member;
import com.nhnacademy.marketgg.server.entity.Review;
import com.nhnacademy.marketgg.server.repository.asset.AssetRepository;
import com.nhnacademy.marketgg.server.repository.member.MemberRepository;
import com.nhnacademy.marketgg.server.repository.review.ReviewRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@Transactional
class DefaultReviewServiceTest {

    @InjectMocks
    DefaultReviewService reviewService;

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    AssetRepository assetRepository;

    private Member member;
    private Review review;
    private Asset asset;
    private ReviewCreateRequest reviewRequest;
    private ReviewResponse reviewResponse;

    @BeforeEach
    void setUp() {
        MemberCreateRequest memberRequest = new MemberCreateRequest();
        member = new Member(memberRequest);

        asset = Asset.create();

        reviewRequest = new ReviewCreateRequest();
        ReflectionTestUtils.setField(reviewRequest, "assetNo", 1L);
        ReflectionTestUtils.setField(reviewRequest, "content", "리뷰내용");
        ReflectionTestUtils.setField(reviewRequest, "rating", 5L);
        review = new Review(reviewRequest, member, asset);

        reviewResponse = new ReviewResponse(1L,
                                            1L,
                                            1L,
                                            "content",
                                            5L,
                                            true,
                                            LocalDateTime.now(),
                                            LocalDateTime.now(),
                                            null);
    }

    @Test
    @DisplayName("일반 리뷰 생성 성공 테스트")
    void testCreateReview() {
        given(memberRepository.findByUuid(anyString())).willReturn(Optional.ofNullable(member));
        given(assetRepository.findById(anyLong())).willReturn(Optional.ofNullable(asset));
        given(reviewRepository.save(any(Review.class))).willReturn(review);

        this.reviewService.createReview(reviewRequest, "admin");

        then(reviewRepository).should().save(any(Review.class));
    }

    @Test
    @DisplayName("후기 전체 조회 테스트")
    void testRetrieveReviews() {
        List<ReviewResponse> list = List.of(reviewResponse);
        Page page = new PageImpl<>(list, PageRequest.of(0, 1), 1);
        given(reviewRepository.retrieveReviews(PageRequest.of(0, 1))).willReturn(page);

        SingleResponse<Page> reviewResponses = reviewService.retrieveReviews(page.getPageable());

        assertThat(reviewResponses).isNotNull();
        then(reviewRepository).should().retrieveReviews(page.getPageable());
    }

    @Test
    @DisplayName("후기 상세 조회 테스트")
    void testRetrieveReviewDetails() {
        given(reviewRepository.queryById(anyLong())).willReturn(reviewResponse);

        SingleResponse<ReviewResponse> response = reviewService.retrieveReviewDetails(1L);

        assertThat(response.getData().getContent()).isEqualTo("content");
        then(reviewRepository).should().queryById(anyLong());
    }

}
