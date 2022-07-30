package com.nhnacademy.marketgg.server.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ReviewResponse {

    private final Long id;

    private final Long memberId;

    private final Long assetId;

    private final String content;

    private final Long rating;

    private final Boolean isBest;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    private final LocalDateTime deletedAt;

}
