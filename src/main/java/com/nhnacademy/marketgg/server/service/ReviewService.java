package com.nhnacademy.marketgg.server.service;

import com.nhnacademy.marketgg.server.dto.request.ReviewCreateRequest;

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
     * @param uuid - 회원을 찾기 위한 uuid값입니다.
     */
    void createReview(final ReviewCreateRequest reviewRequest, final String uuid);

}
