package com.nhnacademy.marketgg.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.marketgg.server.dto.request.PointHistoryRequest;
import com.nhnacademy.marketgg.server.dto.request.ShopMemberSignupRequest;
import com.nhnacademy.marketgg.server.dto.response.ShopMemberSignupResponse;
import com.nhnacademy.marketgg.server.entity.PointHistory;
import com.nhnacademy.marketgg.server.service.MemberService;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import com.nhnacademy.marketgg.server.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 회원관리에 관련된 RestController 입니다.
 *
 * @version 1.0.0
 */
@RestController
@RequestMapping("/shop/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PointService pointService;

    /**
     * 선택한 회원의 GG 패스 갱신일시를 반환하는 Mapping 을 지원합니다.
     *
     * @param memberId - GG 패스 갱신일시를 확인 할 회원의 식별번호입니다.
     * @return 선택한 회원의 GG 패스 갱신일을 반환합니다.
     * @since 1.0.0
     */
    @GetMapping("/{memberId}/ggpass")
    public ResponseEntity<LocalDateTime> retrievePassUpdatedAt(@PathVariable final Long memberId) {
        LocalDateTime check = memberService.retrievePassUpdatedAt(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                             .location(URI.create("/shop/v1/members/" + memberId + "/ggpass"))
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(check);
    }

    /**
     * 선택한 회원을 GG 패스에 구독시키는 Mapping 을 지원합니다.
     *
     * @param memberId - GG 패스를 구독할 회원의 식별번호입니다.
     * @return Mapping URI 를 담은 응답 객체를 반환합니다.
     * @since 1.0.0
     */
    @PostMapping("/{memberId}/ggpass/subscribe")
    public ResponseEntity<Void> subscribePass(@PathVariable final Long memberId) {
        memberService.subscribePass(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                             .location(URI.create("/shop/v1/members/" + memberId + "/ggpass/subscribe"))
                             .contentType(MediaType.APPLICATION_JSON)
                             .build();
    }

    /**
     * 선택한 회원을 GG 패스에 구독해지시키는 Mapping 을 지원합니다.
     *
     * @param memberId - GG 패스를 구독해지할 회원의 식별번호입니다.
     * @return Mapping URI 를 담은 응답 객체를 반환합니다.
     * @since 1.0.0
     */
    @PostMapping("/{memberId}/ggpass/withdraw")
    public ResponseEntity<Void> withdrawPass(@PathVariable final Long memberId) {
        memberService.withdrawPass(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                             .location(URI.create("/shop/v1/members/" + memberId + "/ggpass/withdraw"))
                             .contentType(MediaType.APPLICATION_JSON)
                             .build();
    }

    /**
     * Client 에서 받은 회원가입 Form 에서 입력한 정보로 회원가입을 하는 로직입니다.
     *
     * @param shopMemberSignupRequest - shop
     * @return Mapping URI 를 담은 응답 객체를 반환합니다.
     * @since 1.0.0
     */
    @PostMapping("/signup")
    public ResponseEntity<Void> doSignup(@RequestBody final ShopMemberSignupRequest shopMemberSignupRequest) {
        ShopMemberSignupResponse signup = memberService.signup(shopMemberSignupRequest);

        if (Objects.nonNull(signup.getReferrerMemberId())) {
            pointService.createPointHistory(signup.getReferrerMemberId(),new PointHistoryRequest(5000,"추천인 이벤트"));
        }

        pointService.createPointHistory(signup.getSignupMemberId(), new PointHistoryRequest(5000, "회원 가입"));

        return ResponseEntity.status(HttpStatus.OK)
                             .location(URI.create("/shop/v1/members/signup"))
                             .contentType(MediaType.APPLICATION_JSON)
                             .build();
    }

}
