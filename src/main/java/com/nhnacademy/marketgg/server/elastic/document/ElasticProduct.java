package com.nhnacademy.marketgg.server.elastic.document;

import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Document(indexName = "products")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class ElasticProduct {

    @Id
    @Field
    private Long id;

    @Field
    private String categoryCode;

    @Field
    private String productName;

    @Field
    private String content;

    @Field
    private String description;

    @Field
    private String labelName;

    @Field
    private String imageAddress;

    @Field
    private String price;

    @Field
    private String amount;

}
