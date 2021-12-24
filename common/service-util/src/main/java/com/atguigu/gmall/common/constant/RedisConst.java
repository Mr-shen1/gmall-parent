package com.atguigu.gmall.common.constant;

/**
 * Redis常量配置类
 * set name admin
 */
public class RedisConst {

    public static final String SKUKEY_PREFIX = "sku:";
    public static final String SKUKEY_SUFFIX = ":info";
    //单位：秒
    public static final long SKUKEY_TIMEOUT = 24 * 60 * 60;
    // 定义变量，记录空对象的缓存过期时间
    public static final long SKUKEY_TEMPORARY_TIMEOUT = 10 * 60;

    //单位：秒 尝试获取锁的最大等待时间
    public static final long SKULOCK_EXPIRE_PX1 = 1;
    //单位：秒 锁的持有时间
    public static final long SKULOCK_EXPIRE_PX2 = 1;
    public static final String SKULOCK_SUFFIX = ":lock";

    public static final String USER_KEY_PREFIX = "user:";
    public static final String USER_CART_KEY_SUFFIX = ":cart";
    public static final long USER_CART_EXPIRE = 60 * 60 * 24 * 7;

    //用户登录
    public static final String USER_LOGIN_KEY_PREFIX = "user:login:";
    //    public static final String userinfoKey_suffix = ":info";
    public static final int USERKEY_TIMEOUT = 60 * 60 * 24 * 7;

    //秒杀商品前缀
    public static final String SECKILL_GOODS = "seckill:goods";
    public static final String SECKILL_ORDERS = "seckill:orders";
    public static final String SECKILL_ORDERS_USERS = "seckill:orders:users";
    public static final String SECKILL_STOCK_PREFIX = "seckill:stock:";
    public static final String SECKILL_USER = "seckill:user:";
    //用户锁定时间 单位：秒
    public static final int SECKILL__TIMEOUT = 60 * 60 * 1;


    public static final String INDEX_CACHE_PREFIX = "index:cache:";
    public static final String SKU_CACHE_PREFIX = "sku:cache:";

    public static final String CATEGORY = "categories";
    public static final String ITEMS_PREFIX = "items:";

    public static final Long INDEX_CACHE_TIMEOUT = 60 * 60 * 24 * 2L;
    public static final Long NULL_TIMEOUT = 60 * 30L;

    public static final String LOCK_PREFIX = "lock:";
    public static final String BLOOM_REBUILD_LOCK = "bloom_filter_rebuild";
    public static final Long BLOOM_REBUILD_REBUILD_TIME = 60 * 60 * 24L;
    public static final String BLOOM_STATUS = "bloom:filter:rebuild:status";
    public static final String SKU_LOCK = "sku:";
    public static final String SKU_BLOOM_FILTER = "sku_bloom_filter";

    public static final String SKU_HOT_SCORE = "sku:hotscore";

    public static final String USER_CART_KEY_PREFIX = "cart:info:";

    public static final Long CART_MAX_SIZE = 200L;


    // order
    public static final String ORDER_TRADE_PREFIX = "ATGUIGU";

    public static final String ORDER_TEMP_TOKEN = "order:user:token:";  //order:user:token:1  == tradeNo

}
