package com.nhnacademy.marketgg.server.service;

import com.nhnacademy.marketgg.server.dto.request.ReviewCreateRequest;
import com.nhnacademy.marketgg.server.dto.request.ReviewUpdateRequest;
import com.nhnacademy.marketgg.server.dto.response.ReviewResponse;
import com.nhnacademy.marketgg.server.dto.response.common.SingleResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * 리뷰 서비스입니다.
 *
 * @version 1.0.0
 */
public interface ReviewService {

    /**
     * 글로 작성된 리뷰를 생성합니다.
     *
     * @param reviewRequest - 리뷰 생성을 위한 dto입니다.
     * @param uuid          - 회원을 찾기 위한 uuid값입니다.
     */
    void createReview(final ReviewCreateRequest reviewRequest, List<MultipartFile> images, final String uuid)
        throws IOException;

    /**
     * 모든 리뷰를 조회합니다.
     *
     * @param pageable - 사이즈는 10입니다.
     * @return - 페이지 정보가 담긴 공통 응답객체를 반환합니다.
     */
    SingleResponse<Page<ReviewResponse>> retrieveReviews(final Pageable pageable);

    /**
     * 후기의 상세 정보를 조회합니다.
     *
     * @param id - 후기의 기본 키 입니다.
     * @return - ReviewResponse가 담긴 공통 응답객체를 반환합니다.
     */
    SingleResponse<ReviewResponse> retrieveReviewDetails(final Long id);

    /**
     * 후기를 수정합니다.
     *
     * @param reviewRequest - 후기 수정 정보를 담은 DTO입니다.
     * @param id            - 후기의 식별번호입니다.
     */
    void updateReview(final ReviewUpdateRequest reviewRequest, final Long id);

    /**
     * 후기를 삭제합니다.
     *
     * @param id - 삭제하려는 후기의 기본키입니다.
     */
    void deleteReview(final Long id);

    /**
     * 관리자가 후기를 승인합니다.
     *
     * @param id - 승인하려는 후기의 기본키입니다.
     */
    boolean approveReview(final Long id);

}
