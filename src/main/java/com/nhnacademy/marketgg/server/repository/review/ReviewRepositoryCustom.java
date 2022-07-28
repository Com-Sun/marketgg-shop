package com.nhnacademy.marketgg.server.repository.review;

import com.nhnacademy.marketgg.server.dto.response.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 후기를 관리하는 저장소입니다.
 *
 * @version 1.0.0
 */
@NoRepositoryBean
public interface ReviewRepositoryCustom {

    /**
     * 모든 후기를 조회합니다.
     *
     * @param pageable - 페이지번호 0, 사이즈 10을 가지고있습니다.
     * @return - 페이지 객체를 반환합니다.
     */
    Page<ReviewResponse> retrieveReviews(Pageable pageable);
}
