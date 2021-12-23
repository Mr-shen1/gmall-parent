package com.atguigu.gmall.cart.service.impl;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.client.product.ProductFeignClient;
import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.product.SkuInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/22
 */
@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ProductFeignClient productFeignClient;

    ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public String determineCartKey(String userId, String userTempId) {

        String cartKey = !StringUtils.isEmpty(userId) ? userId : userTempId;

        return RedisConst.USER_CART_KEY_PREFIX + cartKey;
    }


    @Override
    public SkuInfo addCart(Long skuId, Integer skuNum, String cartKey) throws JsonProcessingException {
        log.info("addCart() called with parameters => 【skuId = {}】, 【skuNum = {}】, 【cartKey = {}】", skuId, skuNum, cartKey);
        //判断是否购物车已达最大数量
        checkCartSize(cartKey);

        // 根据 skuId 获取 SkuInfo 信息
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);

        // 1.1 查出这个商品
        String json = (String) stringRedisTemplate.opsForHash().get(cartKey, skuId.toString());

        CartInfo cartInfo;
        if (!StringUtils.isEmpty(json)) { // 购物车中存在这个商品 加数量
            cartInfo = objectMapper.readValue(json, CartInfo.class);
            cartInfo.setSkuNum(cartInfo.getSkuNum() + skuNum);
            cartInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));

            // 再查下实时价格
            cartInfo.setSkuPrice(skuInfo.getPrice());

        } else { // 购物车中没有这个商品

            cartInfo = convertSkuInfo2CartInfo(skuNum, skuInfo);


        }
        // 将购物车信息存入redis
        saveCartInfo(skuId, cartKey, cartInfo);

        log.info("addCart() returned: " + skuInfo);
        return skuInfo;
    }


    @Override
    public List<CartInfo> getCartList(String cartKey) {

        log.info("cartList() called with parameters => 【cartKey = {}】", cartKey);
        List<Object> cartList = stringRedisTemplate.opsForHash().values(cartKey);
        List<CartInfo> resultList = null;
        if (!CollectionUtils.isEmpty(cartList)) {
            resultList = cartList.stream().map(e -> {
                CartInfo cartInfo = null;
                String json = e.toString();
                try {
                    cartInfo = objectMapper.readValue(json, CartInfo.class);
                } catch (JsonProcessingException jsonProcessingException) {
                    log.error("cartList() called with exception => 【cartKey = {}】", cartKey, e);
                }
                return cartInfo;
            }).sorted(((o1, o2) -> (int) (o2.getCreateTime().getTime() - o1.getCreateTime().getTime()))).collect(Collectors.toList());

        }
        return resultList;

    }

    @Override
    public void add2CartNum(String cartKey, Long skuId, Long num) throws JsonProcessingException {
        CartInfo cartInfo = getCartInfo(cartKey, skuId);
        cartInfo.setSkuNum(cartInfo.getSkuNum() + num.intValue());

        saveCartInfo(skuId, cartKey, cartInfo);
    }

    @Override
    public void deleteCart(String cartKey, Long skuId) {
        stringRedisTemplate.opsForHash().delete(cartKey, skuId.toString());

    }

    @Override
    public void updateCartCheckStatus(String cartKey, Long skuId, Long status) throws JsonProcessingException {
        CartInfo cartInfo = getCartInfo(cartKey, skuId);

        cartInfo.setIsChecked(status.intValue());
        saveCartInfo(skuId, cartKey, cartInfo);


    }

    @Override
    public void checkCartSize(String cartKey) {
        // 获取redis中存的数量
        Long size = stringRedisTemplate.opsForHash().size(cartKey);
        if (size > RedisConst.CART_MAX_SIZE) {
            throw new GmallException(ResultCodeEnum.CART_OVERFLOW_MAXSIZE);
        }
    }

    @Override
    public void mergeCart(String userId, String userTempId) throws JsonProcessingException {

        // 检查合并后的数量
        checkAllSize(userId, userTempId);

        if (!StringUtils.isEmpty(userId) && !StringUtils.isEmpty(userTempId)) {
            // 需要合并
            // 获取临时购物车信息

            String tempCartKey = RedisConst.USER_CART_KEY_PREFIX + userTempId;
            List<CartInfo> tempCartList = getCartList(tempCartKey);
            if (!CollectionUtils.isEmpty(tempCartList)) {
                for (CartInfo cartInfo : tempCartList) {
                    // 将临时购物车添加到用户购物车
                    addCart(cartInfo.getSkuId(), cartInfo.getSkuNum(), RedisConst.USER_CART_KEY_PREFIX + userId);
                }
                // 清空临时购物车
                stringRedisTemplate.delete(tempCartKey);
            }
        }
    }

    /**
     * 检查合并后的数量
     *
     * @param userId
     * @param userTempId
     */
    private void checkAllSize(String userId, String userTempId) {

        // 获取数量
        Long userIdSize = stringRedisTemplate.opsForHash().size(RedisConst.USER_CART_KEY_PREFIX + userId);
        Long tempSize = stringRedisTemplate.opsForHash().size(RedisConst.USER_CART_KEY_PREFIX + userTempId);

        if (userIdSize + tempSize > 200) {
            throw new GmallException(ResultCodeEnum.CART_OVERFLOW_MAXSIZE);
        }

    }

    /**
     * 获取cartInfo信息
     *
     * @param cartKey
     * @param skuId
     * @return
     * @throws JsonProcessingException
     */
    private CartInfo getCartInfo(String cartKey, Long skuId) throws JsonProcessingException {
        String json = (String) stringRedisTemplate.opsForHash().get(cartKey, skuId.toString());
        return objectMapper.readValue(json, CartInfo.class);
    }

    /**
     * 保存cartInfo信息到redis
     *
     * @param skuId
     * @param cartKey
     * @param cartInfo
     * @throws JsonProcessingException
     */
    private void saveCartInfo(Long skuId, String cartKey, CartInfo cartInfo) throws JsonProcessingException {
        stringRedisTemplate.opsForHash().put(cartKey, skuId.toString(), objectMapper.writeValueAsString(cartInfo));
    }

    /**
     * 将skuInfo转为cartInfo
     *
     * @param skuNum
     * @param skuInfo
     * @return
     */
    private CartInfo convertSkuInfo2CartInfo(Integer skuNum, SkuInfo skuInfo) {

        // 添加购物车信息
        CartInfo cartInfo = new CartInfo();
        cartInfo.setSkuId(skuInfo.getId());
        cartInfo.setCartPrice(skuInfo.getPrice());
        cartInfo.setSkuNum(skuNum);
        cartInfo.setImgUrl(skuInfo.getSkuDefaultImg());
        cartInfo.setSkuName(skuInfo.getSkuName());
        cartInfo.setIsChecked(1);
        cartInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
        cartInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        cartInfo.setSkuPrice(skuInfo.getPrice());
        return cartInfo;
    }


}
