package com.atguigu.gmall.order.service.impl;

import com.atguigu.gmall.client.cart.CartFeignClient;
import com.atguigu.gmall.client.product.ProductFeignClient;
import com.atguigu.gmall.client.user.UserFeignClient;
import com.atguigu.gmall.client.ware.WareFeignClient;
import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.AuthContextHolder;
import com.atguigu.gmall.model.api.ConfirmOrderVo;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.enums.OrderStatus;
import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.order.OrderDetail;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.order.OrderStatusLog;
import com.atguigu.gmall.model.user.UserAddress;
import com.atguigu.gmall.order.mapper.OrderDetailMapper;
import com.atguigu.gmall.order.mapper.OrderInfoMapper;
import com.atguigu.gmall.order.mapper.OrderStatusLogMapper;
import com.atguigu.gmall.order.properties.OrderProperties;
import com.atguigu.gmall.order.service.OrderDetailService;
import com.atguigu.gmall.order.service.OrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author SSS
 * @description 针对表【order_info(订单表 订单表)】的数据库操作Service实现
 * @createDate 2021-12-24 18:10:34
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo>
        implements OrderInfoService {


    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private CartFeignClient cartFeignClient;
    @Autowired
    private ProductFeignClient productFeignClient;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private WareFeignClient wareFeignClient;
    @Autowired
    private OrderProperties orderProperties;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrderStatusLogMapper orderStatusLogMapper;
    @Autowired
    private OrderInfoService orderInfoService;

    @Override
    public ConfirmOrderVo checkoutCart() {

        ConfirmOrderVo confirmOrderVo = new ConfirmOrderVo();

        // 查询购物车中选中的商品
        List<CartInfo> cartInfoList = cartFeignClient.getIsChecked();

        // 1 设置返回的详情列表
        List<OrderDetail> orderDetailList = cartInfoList.stream().map(cartInfo -> {

            return getOrderDetail(cartInfo);
        }).collect(Collectors.toList());

        confirmOrderVo.setDetailArrayList(orderDetailList);

        // 2 返回总数 和 总金额
        Integer totalNum = 0;
        BigDecimal totalAmount = new BigDecimal("0.0");
        for (OrderDetail orderDetail : orderDetailList) {
            totalNum += orderDetail.getSkuNum();
            BigDecimal price = orderDetail.getOrderPrice().multiply(new BigDecimal(orderDetail.getSkuNum().toString()));
            totalAmount = totalAmount.add(price);
        }
        confirmOrderVo.setTotalNum(totalNum);
        confirmOrderVo.setTotalAmount(totalAmount);

        // 3 返回用户的所有地址信息
        List<UserAddress> userAddressList = userFeignClient.getUserAddressList();
        confirmOrderVo.setUserAddressList(userAddressList);

        // 4 返回tradeNo
        String tradeNo = generateTradeNo();
        confirmOrderVo.setTradeNo(tradeNo);

        return confirmOrderVo;
    }

    private OrderDetail getOrderDetail(CartInfo cartInfo) {
        OrderDetail orderDetail = new OrderDetail();
        BeanUtils.copyProperties(cartInfo, orderDetail);
        // 查库存
        String hasStock = wareFeignClient.hasStock(cartInfo.getSkuId(), cartInfo.getSkuNum());
        orderDetail.setHasStock(hasStock);

        // 实时查价格
        BigDecimal skuPrice = productFeignClient.getSkuPriceById(cartInfo.getSkuId());
        orderDetail.setOrderPrice(skuPrice);

        return orderDetail;
    }


    @Override
    public Long submitOrder(OrderInfo orderInfo, String tradeNo, String userId) {
        // 1 验证令牌是否重复提交
        String tokenKey = RedisConst.ORDER_TEMP_TOKEN + userId;

        // 1.1 去redis中删除token, 如果不能删除, 则说明订单已经提交过了
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Long execute = stringRedisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Arrays.asList(tokenKey), tradeNo);
        // 1.2 抛出异常, 在controller层做统一处理
        if (execute == 0) {
            throw new GmallException(ResultCodeEnum.ORDER_SUBMIT_REPEAT);
        }

        // 2 验库存
        List<OrderDetail> orderDetailList = orderInfo.getOrderDetailList();
        // 没有库存的商品
        List<OrderDetail> noStock = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetailList) {

            String hasStock = wareFeignClient.hasStock(orderDetail.getSkuId(), orderDetail.getSkuNum());
            if ("0".equals(hasStock)) {
                noStock.add(orderDetail);
            }
        }

        if (noStock.size() > 0) {
            // 没库存
            String noStockSkuNames = noStock.stream()
                    .map(OrderDetail::getSkuName)
                    .reduce((a, b) -> {
                        return a + "/n" + b;
                    }).get();
            throw new GmallException("以下商品库存不足:【" + noStockSkuNames + "】", ResultCodeEnum.PRODUCT_NO_STOCK.getCode());
        }

        // 3 验价格
        List<OrderDetail> priceChanged = new ArrayList<>();

        for (OrderDetail orderDetail : orderDetailList) {
            BigDecimal price = orderDetail.getOrderPrice();
            BigDecimal realPrice = productFeignClient.getSkuPriceById(orderDetail.getSkuId());
            if (!realPrice.equals(price)) {
                // 价格不一样
                priceChanged.add(orderDetail);
            }
        }

        if (priceChanged.size() > 0) {
            String s = priceChanged.stream()
                    .map(OrderDetail::getSkuName)
                    .reduce((a, b) -> a + "/n" + b).get();
                    // 价格如果发生了变化则将令牌在设置回去

            throw new GmallException("以下商品的价格发生了变化: 【" + s + "】", ResultCodeEnum.PRODUCT_PRICE_CHANGED.getCode());
        }

        // 如果以上都通过, 则基本校验完成
        // 4 创建订单, 将orderInfo 的内容保存到数据库

        // 这样直接调用会导致事务失效
        //Long orderId = saveOrder(orderInfo, tradeNo);

        // 注入代理类保证事务有效
        Long orderId = orderInfoService.saveOrder(orderInfo, tradeNo);

        return orderId;
    }

    /**
     * 将订单保存到数据库
     *
     * @param orderInfo
     * @param tradeNo
     */
    @Transactional
    @Override
    public Long saveOrder(OrderInfo orderInfo, String tradeNo) {
        //1 准备订单 (填充前端没提交的字段)

        OrderInfo order = prepareOrder(orderInfo, tradeNo);
        // 存
        // TODO 直接使用service会导致事务失效吗
        orderInfoMapper.insert(order);
        // 获取自增id(订单id)
        Long orderId = order.getId();

        //2 处理orderDetail
        List<OrderDetail> orderDetailList = orderInfo.getOrderDetailList();
        List<OrderDetail> detailList = orderDetailList.stream()
                .map(e -> {
                    e.setOrderId(orderId);
                    // TODO 我的时间不是当前时间 待排查
                    e.setCreateTime(new Date());
                    return e;
                }).collect(Collectors.toList());
        // 批量处理
        orderDetailService.saveBatch(detailList);

        //3 存 order_status_log
        OrderStatusLog orderStatusLog = new OrderStatusLog();
        orderStatusLog.setOrderId(orderId);
        orderStatusLog.setOrderStatus(OrderStatus.UNPAID.name());
        orderStatusLog.setOperateTime(new Date());

        orderStatusLogMapper.insert(orderStatusLog);


        // 成功保存完订单后, 删除购物车
        String userId = AuthContextHolder.getUserId();
        for (OrderDetail orderDetail : orderDetailList) {
            remove(orderDetail.getSkuId(), userId);
        }

        return orderId;

    }

    /**
     * 删除购物车信息
     * @param skuId
     * @param userId
     */
    private void remove(Long skuId, String userId) {
        stringRedisTemplate.opsForHash().delete(RedisConst.USER_CART_KEY_PREFIX + userId, skuId.toString());

    }

    /**
     * 准备订单 (填充前端没提交的字段)
     *
     * @param orderInfo 前端提交的
     * @param tradeNo
     * @return
     */
    private OrderInfo prepareOrder(OrderInfo orderInfo, String tradeNo) {

        OrderInfo resultOrderInfo = new OrderInfo();

        // 复制
        BeanUtils.copyProperties(orderInfo, resultOrderInfo);

        resultOrderInfo.sumTotalAmount(); // javaBean 中的方法

        resultOrderInfo.setOrderStatus(OrderStatus.UNPAID.name()); // 数据库要存的是名字, 不是内容

        String userId = AuthContextHolder.getUserId();
        resultOrderInfo.setUserId(Long.valueOf(userId));

        resultOrderInfo.setOutTradeNo(tradeNo);

        List<OrderDetail> orderDetailList = orderInfo.getOrderDetailList();

        String tradeBody = orderDetailList.stream()
                .map(OrderDetail::getSkuName)
                .reduce((a, b) -> a + "/n" + b).get();

        resultOrderInfo.setTradeBody(tradeBody); //skuName

        resultOrderInfo.setCreateTime(new Date());

        // 订单过期时间(提交后多久没付款就过期)

        resultOrderInfo.setExpireTime(new Date(System.currentTimeMillis() + orderProperties.getTimeout() * 1000));

        resultOrderInfo.setProcessStatus(ProcessStatus.UNPAID.name());
        resultOrderInfo.setTrackingNo("");
        resultOrderInfo.setParentOrderId(null);

        String imgUrl = orderDetailList.get(0).getImgUrl();
        resultOrderInfo.setImgUrl(imgUrl);
        resultOrderInfo.setOperateTime(new Date());

        return resultOrderInfo;
    }


    /**
     * 生成tradeNo
     *
     * @return
     */
    private String generateTradeNo() {
        // 获取用户id
        String userId = AuthContextHolder.getUserId();
        // 生成tradeNo
        String tradeNo = RedisConst.ORDER_TRADE_PREFIX + System.currentTimeMillis() + userId;

        // 在生成tradeNo的同时, 往redis存一份 用来防止订单重复提交
        String tokenKey = RedisConst.ORDER_TEMP_TOKEN + userId;
        stringRedisTemplate.opsForValue().set(tokenKey, tradeNo, 1800, TimeUnit.SECONDS);

        return tradeNo;
    }
}




