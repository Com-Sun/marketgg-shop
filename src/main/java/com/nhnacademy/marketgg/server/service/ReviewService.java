package com.nhnacademy.marketgg.server.service;

import com.nhnacademy.marketgg.server.dto.request.ReviewCreateRequest;
import com.nhnacademy.marketgg.server.dto.response.common.SingleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    void createReview(final ReviewCreateRequest reviewRequest, final String uuid);

    /**
     * 모든 리뷰를 조회합니다.
     *
     * @param pageable - 사이즈는 10입니다.
     * @return - 페이지 정보가 담긴 공통 응답객체를 반환합니다.
     */
    SingleResponse<Page> retrieveReviews(final Pageable pageable);

}
