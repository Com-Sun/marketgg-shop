package com.nhnacademy.marketgg.server.repository.review;

import com.nhnacademy.marketgg.server.dto.response.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ReviewRepositoryCustom {

    Page<ReviewResponse> retrieveReviews(Pageable pageable);
}
