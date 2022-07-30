package com.nhnacademy.marketgg.server.entity;

import com.nhnacademy.marketgg.server.dto.request.ReviewCreateRequest;
import com.nhnacademy.marketgg.server.dto.request.ReviewUpdateRequest;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "reviews")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_no")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_no")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "asset_no")
    private Asset asset;

    @Column
    private String content;

    @Column
    private Long rating;

    @Column(name = "is_best")
    private Boolean isBest;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public Review(ReviewCreateRequest reviewRequest, Member member, Asset asset) {
        this.member = member;
        this.asset = asset;
        this.content = reviewRequest.getContent();
        this.rating = reviewRequest.getRating();
        this.isBest = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateReview(ReviewUpdateRequest reviewRequest, Asset asset) {
        this.content = reviewRequest.getContent();
        this.rating = reviewRequest.getRating();
        this.asset = asset;
    }

}
