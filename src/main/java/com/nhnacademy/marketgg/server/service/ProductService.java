package com.nhnacademy.marketgg.server.service;

import com.nhnacademy.marketgg.server.dto.request.ProductCreateRequest;
import com.nhnacademy.marketgg.server.dto.request.ProductUpdateRequest;
import com.nhnacademy.marketgg.server.dto.response.ProductResponse;
import java.util.List;

public interface ProductService {

    void createProduct(ProductCreateRequest productRequest);

    List<ProductResponse> retrieveProducts();

    void updateProduct(ProductUpdateRequest productRequest, Long productId);

    void deleteProduct(Long productId);

}
