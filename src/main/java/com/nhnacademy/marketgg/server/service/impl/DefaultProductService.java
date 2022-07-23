package com.nhnacademy.marketgg.server.service.impl;

import com.nhnacademy.marketgg.server.dto.request.ProductCreateRequest;
import com.nhnacademy.marketgg.server.dto.request.ProductUpdateRequest;
import com.nhnacademy.marketgg.server.dto.response.ProductResponse;
import com.nhnacademy.marketgg.server.entity.Asset;
import com.nhnacademy.marketgg.server.entity.Category;
import com.nhnacademy.marketgg.server.entity.Image;
import com.nhnacademy.marketgg.server.entity.Product;
import com.nhnacademy.marketgg.server.exception.category.CategoryNotFoundException;
import com.nhnacademy.marketgg.server.exception.product.ProductNotFoundException;
import com.nhnacademy.marketgg.server.repository.asset.AssetRepository;
import com.nhnacademy.marketgg.server.repository.category.CategoryRepository;
import com.nhnacademy.marketgg.server.repository.image.ImageRepository;
import com.nhnacademy.marketgg.server.repository.product.ProductRepository;
import com.nhnacademy.marketgg.server.service.ProductService;
import java.io.File;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final AssetRepository assetRepository;
    private final ImageRepository imageRepository;

    @Value("${uploadPath}")
    private String uploadPath;

    @Override
    @Transactional
    public void createProduct(final ProductCreateRequest productRequest, MultipartFile imageFile)
        throws IOException {

        String dir = System.getProperty("user.home");
        String originalFileName = imageFile.getOriginalFilename();
        File dest = new File(dir, originalFileName);
        imageFile.transferTo(dest);

        Asset asset = this.assetRepository.save(Asset.create());
        Image image = new Image(asset, dest.toString());
        this.imageRepository.save(image);

        Category category = this.categoryRepository.findById(productRequest.getCategoryCode()).orElseThrow(CategoryNotFoundException::new);

        this.productRepository.save(new Product(productRequest, asset, category));
    }

    @Override
    public Page<ProductResponse> retrieveProducts(Pageable pageable) {
        return productRepository.findAllProducts(pageable);
    }

    @Override
    public ProductResponse retrieveProductDetails(final Long productId) {
        return this.productRepository.queryById(productId);
    }

    @Override
    @Transactional
    public void updateProduct(final ProductUpdateRequest productRequest, MultipartFile imageFile,
                              final Long productId) throws IOException {
        Product product =
            this.productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        String originalFileName = imageFile.getOriginalFilename();
        File dest = new File(uploadPath, originalFileName);
        imageFile.transferTo(dest);

        Asset asset = this.assetRepository.save(Asset.create());
        Image image = new Image(asset, dest.toString());
        this.imageRepository.save(image);

        Category category = this.categoryRepository.findById(productRequest.getCategoryCode())
                                                   .orElseThrow(CategoryNotFoundException::new);

        product.updateProduct(productRequest, asset, category);
        this.productRepository.save(product);
    }

    @Override
    public void deleteProduct(final Long productId) {
        Product product =
            this.productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        product.deleteProduct();
        this.productRepository.save(product);
    }

    @Override
    public List<ProductResponse> searchProductsByName(final String keyword) {
        return this.productRepository.findByNameContaining(keyword);
    }

    @Override
    public Page<ProductResponse> searchProductByCategory(final String categoryCode, final Pageable pageable) {
        return productRepository.findByCategoryCode(categoryCode, pageable);

    }

}
