package com.atguigu.gmall.gateway.filter;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.IpUtil;
import com.atguigu.gmall.gateway.properties.FilterProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/21
 */
@Component
@Slf4j
public class RequestAuthFilter implements GlobalFilter {


    @Autowired
    private FilterProperties filterProperties;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    String innerPath = "/**/inner/**";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();

        // 1拦截内部feign接口调用url
        if (antPathMatcher.match(innerPath, path)) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            Result fail = Result.fail();
            fail.setMessage("拒绝访问");
            String jsonResponse = "";

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                jsonResponse = objectMapper.writeValueAsString(fail);
            } catch (JsonProcessingException e) {
            }
            DefaultDataBufferFactory defaultDataBufferFactory = new DefaultDataBufferFactory();

            DefaultDataBuffer dataBuffer = defaultDataBufferFactory.wrap(jsonResponse.getBytes(StandardCharsets.UTF_8));

            Mono<DefaultDataBuffer> just = Mono.just(dataBuffer);
            // 处理乱码
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            return response.writeWith(just);
        }


        boolean authStatus = false;
        List<String> auth = filterProperties.getAuth();
        // 校验路径是否需要登录(拦截)
        for (String pattern : auth) {
            authStatus = antPathMatcher.match(pattern, path);
            if (authStatus) {
                break;
            }
        }

        // 拦截下来的, 需要登录的
        if (authStatus) {
            // 请求认证
            // 1 拿到前端带来的token
            String token = getToken(request);

            if (StringUtils.isEmpty(token)) {
                // 没有token 直接重定向到登录页面
                Mono<Void> mono = redirectToLoginUrl(request, response);
                return mono;
            } else {
                //有token
                //根据token查询userId
                String userId = getUserId(token, request);
                if (StringUtils.isEmpty(userId)) {
                    // token 无效 返回登录界面
                    return redirectToLoginUrl(request, response);
                } else {
                    // token有效 获取userId 并放在 请求头中 透传给下面
                    // 透传 因为直接在请求头中添加会报错, 所以要构建新的请求头
                    ServerHttpRequest req = request.mutate().header("userId", userId).build();
                    // 重新构建新的请求对象
                    ServerWebExchange ex = exchange.mutate().request(req).build();
                    // 放行新的ServerWebExchange对象
                    return chain.filter(ex);

                }
            }
        }
        //不需要登陆的

        return chain.filter(exchange);
    }

    private String getUserId(String token, ServerHttpRequest request) {
        String json = stringRedisTemplate.opsForValue().get(RedisConst.USER_LOGIN_KEY_PREFIX + token);
        if (StringUtils.isEmpty(json)) {
            // redis 无token信息
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> map = null;
        try {
            map = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });

            // 获取当前登录的ip
            String ipAddress = IpUtil.getGatwayIpAddress(request);
            // 获取redis中的ip
            String ip = map.get("ip").toString();
            if (!Objects.equals(ipAddress, ip)) {
                return null;
            } else {
                return map.get("userId").toString();

            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 重定向到登录界面
     *
     * @param request
     * @param response
     * @return
     */
    private Mono<Void> redirectToLoginUrl(ServerHttpRequest request, ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FOUND);
        String originUrl = request.getURI().toString();
        String loginUrl = filterProperties.getLoginUrl() + "?originUrl=" + originUrl;
        response.getHeaders().add("Location", loginUrl);
        Mono<Void> mono = response.setComplete();
        return mono;
    }

    /**
     * 获取token
     *
     * @param request
     * @return
     */
    private String getToken(ServerHttpRequest request) {

        // 从请求头中获取
        List<String> token = request.getHeaders().get("token");
        if (!CollectionUtils.isEmpty(token)) {
            return token.get(0);
        }
        // 从cookie中获取
        List<HttpCookie> cookies = request.getCookies().get("token");
        if (!CollectionUtils.isEmpty(cookies)) {
            for (HttpCookie cookie : cookies) {
                if (Objects.equals("token", cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        // 都没有则返回null
        return null;

    }
}
