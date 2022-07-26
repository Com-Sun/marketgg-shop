package com.nhnacademy.marketgg.server.aop;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AspectUtils {

    public static final String AUTH_ID = "AUTH-ID";
    public static final String WWW_AUTHENTICATION = "WWW-Authentication";

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return requestAttributes.getRequest();
    }

}
