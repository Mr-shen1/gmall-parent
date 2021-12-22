package com.atguigu.gmall.common.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * desc: feign的公共配置
 *
 * @author: skf
 * @date: 2021/12/22
 */
@Configuration
public class FeignCommonConfig {

    /**
     * 定义feign的请求拦截器, 保证 userId 和 userTempId 不丢失
     * @return
     */
    @Bean
    public RequestInterceptor getRequestInterceptor() {
        // TODO 放到common中
        return (template) -> {
            // 为什么可以强转? 因为在 RequestContextListener 存的时候就是 ServletRequestAttributes 类型

            // ServletRequestAttributes attributes = new ServletRequestAttributes(request);
            // request.setAttribute(REQUEST_ATTRIBUTES_ATTRIBUTE, attributes);

            // 获取当前请求
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String userId = request.getHeader("userId");
            String userTempId = request.getHeader("userTempId");

            // 将请求头信息放入模板中
            if (!StringUtils.isEmpty(userId)) {
                template.header("userId", userId);
            }

            if (!StringUtils.isEmpty(userTempId)) {
                template.header("userTempId", userTempId);
            }

        };
    }
}
