package com.nhnacademy.marketgg.server.controller;

import com.nhnacademy.marketgg.server.dto.request.DefaultPageRequest;
import com.nhnacademy.marketgg.server.dto.request.ReviewCreateRequest;
import com.nhnacademy.marketgg.server.dto.request.ReviewUpdateRequest;
import com.nhnacademy.marketgg.server.dto.response.common.CommonResponse;
import com.nhnacademy.marketgg.server.dto.response.common.SingleResponse;
import com.nhnacademy.marketgg.server.service.ReviewService;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 리뷰 관리를 위한 컨트롤러입니다.
 *
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private static final String DEFAULT_REVIEW_URI = "/products/";

    /**
     * 글로 작성된 리뷰를 생성합니다. 추후 사진 기능이 추가될 예정입니다.
     *
     * @param productId     - 후기가 달릴 상품의 PK입니다.
     * @param uuid          - 후기를 작성한 회원을 구별하기 위한 고유값입니다.
     * @param reviewRequest - 후기 생성을 위한 dto 입니다.
     * @param bindingResult - validation 적용을 위한 파라미터입니다.
     * @return Void를 담은 응답객체를 반환합니다.
     */
    @PostMapping(value = "/{productId}/review/{memberUuid}", consumes = { MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Void> createReview(@PathVariable final Long productId,
                                             @PathVariable(name = "memberUuid") final String uuid,
                                             @RequestPart @Valid final ReviewCreateRequest reviewRequest,
                                             BindingResult bindingResult,
                                             @RequestPart(required = false) List<MultipartFile> images) throws IOException {

        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        reviewService.createReview(reviewRequest, images, uuid);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .location(URI.create(DEFAULT_REVIEW_URI + productId + "/review/" + uuid))
                             .contentType(MediaType.APPLICATION_JSON).build();
    }

    /**
     * 모든 리뷰를 조회합니다.
     *
     * @param productId   - 리뷰가 달린 상품의 기본키입니다.
     * @param pageRequest - 기본 번호 0, 사이즈 10인 페이지 요청입니다.
     * @return - 페이지 정보가 담긴 공통 응답 객체를 반환합니다.
     */
    @GetMapping("/{productId}/review")
    public ResponseEntity<CommonResponse> retrieveReviews(@PathVariable final Long productId,
                                                          final DefaultPageRequest pageRequest) {

        SingleResponse<?> response = reviewService.retrieveReviews(pageRequest.getPageable());

        return ResponseEntity.status(HttpStatus.OK)
                             .location(URI.create(DEFAULT_REVIEW_URI + productId + "/review"))
                             .contentType(MediaType.APPLICATION_JSON).body(response);
    }

    /**
     * 후기의 상세정보를 조회합니다.
     *
     * @param productId - 후기가 달린 상품의 기본키입니다.
     * @param reviewId  - 작성된 후기의 기본키입니다.
     * @return - ReviewResponse가 담긴 공통 응답객체를 반환합니다.
     */
    @GetMapping("/{productId}/review/{reviewId}")
    public ResponseEntity<CommonResponse> retrieveReviewDetails(@PathVariable final Long productId,
                                                                @PathVariable final Long reviewId) {

        SingleResponse<?> response = reviewService.retrieveReviewDetails(reviewId);

        return ResponseEntity.status(HttpStatus.OK)
                             .location(URI.create(DEFAULT_REVIEW_URI + productId + "/review/" + reviewId))
                             .contentType(MediaType.APPLICATION_JSON).body(response);
    }

    /**
     * 후기를 수정합니다.
     *
     * @param productId     - 후기가 달린 상품의 기본키입니다.
     * @param reviewId      - 후기의 식별번호입니다.
     * @param reviewRequest - 후기 수정 정보가 담긴 DTO 입니다.
     * @param bindingResult - validation을 체크합니다.
     * @return - Void 타입 응답객체를 반환합니다.
     */
    @PutMapping("/{productId}/review/{reviewId}")
    public ResponseEntity<Void> updateReview(@PathVariable final Long productId,
                                             @PathVariable final Long reviewId,
                                             @RequestBody @Valid final ReviewUpdateRequest reviewRequest,
                                             BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        reviewService.updateReview(reviewRequest, reviewId);

        return ResponseEntity.status(HttpStatus.OK)
                             .location(URI.create(DEFAULT_REVIEW_URI + productId + "/review/" + reviewId))
                             .contentType(MediaType.APPLICATION_JSON).build();
    }

    /**
     * 후기를 삭제합니다.
     *
     * @param productId - 후기가 달린 상품의 기본키입니다.
     * @param reviewId  - 후기의 식별번호 입니다.
     * @return - Void 타입 응답객체를 반환합니다.
     */
    @DeleteMapping("/{productId}/review/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable final Long productId,
                                             @PathVariable final Long reviewId) {
        reviewService.deleteReview(reviewId);

        return ResponseEntity.status(HttpStatus.OK)
                             .location(URI.create(DEFAULT_REVIEW_URI + productId + "/review/" + reviewId))
                             .contentType(MediaType.APPLICATION_JSON).build();
    }

}
