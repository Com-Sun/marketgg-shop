package com.nhnacademy.marketgg.server.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.marketgg.server.dto.request.ProductCreateRequest;
import com.nhnacademy.marketgg.server.service.ProductService;
import java.io.FileInputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private Environment environment;

    @MockBean
    private ProductService productService;

    private HttpHeaders headers;
    private static final String DEFAULT_PRODUCT = "/admin/v1/products";
    private static ProductCreateRequest productRequest;
    @Value("${uploadPath}")
    private String uploadPath;

    @BeforeAll
    static void beforeAll() {
        productRequest = new ProductCreateRequest();
        ReflectionTestUtils.setField(productRequest, "categoryCode", "001");
        ReflectionTestUtils.setField(productRequest, "name", "자몽");
        ReflectionTestUtils.setField(productRequest, "content", "아침에 자몽 쥬스");
        ReflectionTestUtils.setField(productRequest, "totalStock", 100L);
        ReflectionTestUtils.setField(productRequest, "price", 2000L);
        ReflectionTestUtils.setField(productRequest, "description", "자몽주스 설명");
        ReflectionTestUtils.setField(productRequest, "unit", "1박스");
        ReflectionTestUtils.setField(productRequest, "deliveryType", "샛별배송");
        ReflectionTestUtils.setField(productRequest, "origin", "인도네시아");
        ReflectionTestUtils.setField(productRequest, "packageType", "냉장");
        ReflectionTestUtils.setField(productRequest, "allergyInfo", "새우알러지");
    }

    @Test
    @DisplayName("property-test")
    void testProperty() {
        String uploadPath = environment.getProperty("uploadPath");
        System.out.println("uploadPath:" + uploadPath);
    }

    @Test
    @DisplayName("상품 등록하는 테스트")
    void testCreateProduct() throws Exception {

        doNothing().when(productService)
                   .createProduct(any(ProductCreateRequest.class), any(MockMultipartFile.class));
        String content = objectMapper.writeValueAsString(productRequest);

        // uploadPath는 자신의 로컬 path로 바꿀 것.
        MockMultipartFile file = new MockMultipartFile
            ("image", "test.png", "image/png",
                new FileInputStream(uploadPath + "/logo.png"));

        MockMultipartFile dto =
            new MockMultipartFile("productRequest", "jsondata", "application/json",
                content.getBytes(StandardCharsets.UTF_8));

        this.mockMvc.perform(multipart("/admin/v1/products")
                .file(dto)
                .file(file)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .characterEncoding(StandardCharsets.UTF_8))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", DEFAULT_PRODUCT));

        verify(productService, times(1)).createProduct(any(ProductCreateRequest.class), any(
            MockMultipartFile.class));
    }

    @Test
    @DisplayName("상품 목록 전체 조회하는 테스트")
    void testRetrieveProducts() throws Exception {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setLocation(URI.create(DEFAULT_PRODUCT));

        this.mockMvc.perform(get("/admin/v1/products"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(header().string("Location", DEFAULT_PRODUCT));
    }

    // @Test
    // @DisplayName("상품 정보 수정하는 테스트")
    // void testUpdateProduct() throws Exception {
    //     headers = new HttpHeaders();
    //     headers.setContentType(MediaType.APPLICATION_JSON);
    //     headers.setLocation(URI.create(DEFAULT_PRODUCT));
    //
    //     doNothing().when(productService).updateProduct(any(), any(), any());
    //     String content = objectMapper.writeValueAsString(productRequest);
    //
    //     this.mockMvc.perform(put("/admin/v1/products" + "/1")
    //                 .contentType(MediaType.APPLICATION_JSON)
    //                 .content(content))
    //                 .andExpect(status().isOk())
    //                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    //                 .andExpect(header().string("Location", DEFAULT_PRODUCT + "/1"));
    // }

    // @Test
    // @DisplayName("상품 삭제하는 테스트")
    // void testDeleteProduct() throws Exception {
    //     headers = new HttpHeaders();
    //     headers.setContentType(MediaType.APPLICATION_JSON);
    //     headers.setLocation(URI.create(DEFAULT_PRODUCT));
    //
    //     this.mockMvc.perform(delete("/admin/v1/products" + "/1"))
    //                 .andExpect(status().isOk())
    //                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    //                 .andExpect(header().string("Location", DEFAULT_PRODUCT + "/1"));
    // }

}
