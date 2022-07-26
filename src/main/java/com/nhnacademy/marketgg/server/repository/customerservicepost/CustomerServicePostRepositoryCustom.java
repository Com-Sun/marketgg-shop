package com.nhnacademy.marketgg.server.repository.customerservicepost;

import com.nhnacademy.marketgg.server.entity.CustomerServicePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomerServicePostRepositoryCustom {
    
    Page<CustomerServicePost> findAllOtoInquiries(Pageable pageable, String categoryId);

    Page<CustomerServicePost> findAllOwnOtoInquiries(Pageable pageable, String categoryId, Long memberId);

    CustomerServicePost findOwnOtoInquiry(Long inquiryId, Long memberId);

}
