package com.nhnacademy.marketgg.server.controller;

import com.nhnacademy.marketgg.server.dto.request.ReviewCreateRequest;
import com.nhnacademy.marketgg.server.service.ReviewService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 글로 작성된 리뷰를 생성합니다. 추후 사진 기능이 추가될 예정입니다.
     *
     * @param productId     - 후기가 달릴 상품의 PK입니다.
     * @param uuid          - 후기를 작성한 회원을 구별하기 위한 고유값입니다.
     * @param reviewRequest - 후기 생성을 위한 dto 입니다.
     * @param bindingResult - validation 적용을 위한 파라미터입니다.
     * @return Void를 담은 응답객체를 반환합니다.
     */
    @PostMapping("/{productId}/review/{memberUuid}")
    public ResponseEntity<Void> createReview(@PathVariable final Long productId,
                                             @PathVariable(name = "memberUuid") final String uuid,
                                             @RequestBody @Valid final ReviewCreateRequest reviewRequest,
                                             BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        this.reviewService.createReview(reviewRequest, uuid);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .location(URI.create("/products/" + productId + "/review/" + uuid))
                             .contentType(MediaType.APPLICATION_JSON)
                             .build();
    }
}
