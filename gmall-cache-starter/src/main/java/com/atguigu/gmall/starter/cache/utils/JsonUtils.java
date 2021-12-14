package com.atguigu.gmall.starter.cache.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Type;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/14
 */
public class JsonUtils {


    /**
     * 将从缓存查出的数据转换为json格式
     * @param methodSignature
     * @param cacheKeyResult
     * @return
     * @throws Exception
     */
    public static Object convertToData(MethodSignature methodSignature, String cacheKeyResult) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(cacheKeyResult, new TypeReference<Object>() {
            @Override
            public Type getType() {
                return methodSignature.getMethod().getReturnType();
            }
        });
    }
}
