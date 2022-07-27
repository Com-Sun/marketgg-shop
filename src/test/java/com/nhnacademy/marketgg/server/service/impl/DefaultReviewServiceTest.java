package com.nhnacademy.marketgg.server.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

import com.nhnacademy.marketgg.server.dto.request.MemberCreateRequest;
import com.nhnacademy.marketgg.server.dto.request.ReviewCreateRequest;
import com.nhnacademy.marketgg.server.entity.Asset;
import com.nhnacademy.marketgg.server.entity.Member;
import com.nhnacademy.marketgg.server.entity.Review;
import com.nhnacademy.marketgg.server.repository.asset.AssetRepository;
import com.nhnacademy.marketgg.server.repository.member.MemberRepository;
import com.nhnacademy.marketgg.server.repository.review.ReviewRepository;
import com.nhnacademy.marketgg.server.service.ReviewService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

}