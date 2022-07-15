package com.nhnacademy.marketgg.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nhnacademy.marketgg.server.dto.request.ProductCreateRequest;
import com.nhnacademy.marketgg.server.dto.request.ProductUpdateRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 엔티티입니다.
 *
 * @since 1.0.0
 */
@Table(name = "products")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_no")
    private Long id;

    @OneToOne
    @JoinColumn(name = "asset_no")
    private Asset asset;

    @ManyToOne
    @JoinColumn(name = "category_code")
    private Category category;

    @Column
    private String name;

    @Column
    private String content;

    @Column(name = "total_stock")
    private Long totalStock;

    @Column
    private Long price;

    @Column
    private String description;

    @Column
    private String unit;

    @Column(name = "delivery_type")
    private String deliveryType;

    @Column
    private String origin;

    @Column(name = "package_type")
    private String packageType;

    @Column(name = "expiration_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;

    @Column(name = "allergy_info")
    private String allergyInfo;

    @Column
    private String capacity;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime deletedAt;

    public Product(ProductCreateRequest productRequest, Asset asset, Category category) {
        this.asset = asset;
        this.category = category;
        this.name = productRequest.getName();
        this.content = productRequest.getContent();
        this.totalStock = productRequest.getTotalStock();
        this.price = productRequest.getPrice();
        this.description = productRequest.getDescription();
        this.unit = productRequest.getUnit();
        this.deliveryType = productRequest.getDeliveryType();
        this.origin = productRequest.getOrigin();
        this.packageType = productRequest.getPackageType();
        this.expirationDate = productRequest.getExpirationDate();
        this.allergyInfo = productRequest.getAllergyInfo();
        this.capacity = productRequest.getCapacity();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateProduct(ProductUpdateRequest productRequest, Asset asset, Category category) {
        this.asset = asset;
        this.category = category;
        this.name = productRequest.getName();
        this.content = productRequest.getContent();
        this.totalStock = productRequest.getTotalStock();
        this.price = productRequest.getPrice();
        this.description = productRequest.getDescription();
        this.unit = productRequest.getUnit();
        this.deliveryType = productRequest.getDeliveryType();
        this.origin = productRequest.getOrigin();
        this.packageType = productRequest.getPackageType();
        this.expirationDate = productRequest.getExpirationDate();
        this.allergyInfo = productRequest.getAllergyInfo();
        this.capacity = productRequest.getCapacity();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 상품의 소프트 삭제를 위한 메서드입니다.
     * null 이 아닌 경우 상품이 삭제된 상태입니다.
     */
    public void deleteProduct() {
        this.deletedAt = LocalDateTime.now();
    }

}
