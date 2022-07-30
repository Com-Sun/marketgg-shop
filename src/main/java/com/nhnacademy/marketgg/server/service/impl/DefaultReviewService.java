package com.nhnacademy.marketgg.server.service.impl;

import com.nhnacademy.marketgg.server.dto.request.ReviewCreateRequest;
import com.nhnacademy.marketgg.server.dto.request.ReviewUpdateRequest;
import com.nhnacademy.marketgg.server.dto.response.ReviewResponse;
import com.nhnacademy.marketgg.server.dto.response.common.SingleResponse;
import com.nhnacademy.marketgg.server.entity.Asset;
import com.nhnacademy.marketgg.server.entity.Image;
import com.nhnacademy.marketgg.server.entity.Member;
import com.nhnacademy.marketgg.server.entity.Review;
import com.nhnacademy.marketgg.server.exception.asset.AssetNotFoundException;
import com.nhnacademy.marketgg.server.exception.member.MemberNotFoundException;
import com.nhnacademy.marketgg.server.exception.review.ReviewNotFoundException;
import com.nhnacademy.marketgg.server.repository.asset.AssetRepository;
import com.nhnacademy.marketgg.server.repository.image.ImageRepository;
import com.nhnacademy.marketgg.server.repository.member.MemberRepository;
import com.nhnacademy.marketgg.server.repository.review.ReviewRepository;
import com.nhnacademy.marketgg.server.service.ReviewService;
import com.nhnacademy.marketgg.server.utils.ImageFileHandler;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * ReviewService의 구현체입니다.
 *
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class DefaultReviewService implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final AssetRepository assetRepository;
    private final ImageRepository imageRepository;

    @Transactional
    @Override
    public void createReview(final ReviewCreateRequest reviewRequest, List<MultipartFile> images,
                             final String uuid) throws IOException {

        Member member = memberRepository.findByUuid(uuid).orElseThrow(MemberNotFoundException::new);

        Asset asset = assetRepository.save(Asset.create());

        List<Image> parseImages = ImageFileHandler.parseImages(images, asset);

        imageRepository.saveAll(parseImages);

        reviewRepository.save(new Review(reviewRequest, member, asset));
    }

    @Override
    public SingleResponse<Page<ReviewResponse>> retrieveReviews(final Pageable pageable) {
        Page<ReviewResponse> response = reviewRepository.retrieveReviews(pageable);

        return new SingleResponse<>(response);
    }


    @Override
    public SingleResponse<ReviewResponse> retrieveReviewDetails(final Long id) {
        ReviewResponse response = reviewRepository.queryById(id);

        return new SingleResponse<>(response);
    }

    @Transactional
    @Override
    public void updateReview(final ReviewUpdateRequest reviewRequest, final Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(ReviewNotFoundException::new);
        Asset asset = assetRepository.findById(reviewRequest.getAssetId())
                                          .orElseThrow(AssetNotFoundException::new);

        review.updateReview(reviewRequest, asset);

        reviewRepository.save(review);

    }

    @Override
    public void deleteReview(final Long id) {
        reviewRepository.delete(reviewRepository.findById(id).orElseThrow(ReviewNotFoundException::new));
    }

    @Override
    public boolean approveReview(Long id) {
        return false;
    }

}
