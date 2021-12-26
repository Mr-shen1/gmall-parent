package com.atguigu.gmall.payment.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/25
 */
@Component
@ConfigurationProperties(prefix = "alipay")
@Data
public class AlipayProperties {









    private String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    private String app_id = "2021000118680203";

    // 商户私钥，您的PKCS8格式RSA2私钥
    String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCXxfuimsQjN/mkJxBH19tz5I974fCBv7aw7BWMSpMscPv0AahP1FCG+0ODB1AN8xXLMcNBkdySEulrqJKjcvxRNc7ZtRUD+3vaev7Nf8xJETT2mSO3aZ9UXP+chnQzIeus+u40qV4+Ehg9ogiY71nx+yyp4uji2722KkieQdk/V5LT5x2m1TVkw2VHIlitcWa8AnBdqE6JdWSQiX3ngK9E1TXxnyfhcgSirrm9WZrEDxjQ9R+yLnKJ7Lco1sos+16zrqDQsidzsinY/aqMhtfTr7+pAQWmN1igF02+bAKLgVM1Re7Qe99HPnaiEtN01o6hQyCFDeA2MfU4h1bstr5hAgMBAAECggEAG3trHD+AAoyRNKsqJIguoaaiYKFB5oyrbQ/iuvuJ+MMgrEdcjc/5IOgAiopZcZ0xthB1tbrOwNiR0b+9hcE9dQsUQJGsk4MHI0GgP9/DQvvNplWGhSCoDCOm2VXzVoru3Oy3E2wTt5OZNhfQDLlShSGhLcX7u5Bkosa3TYjI6NIPQr4QS3Mfoe0jPmNxnoGWAa4snYMmrHiHxbc86fpwflJQ/E5k841JH7WoXxUp6y4dCxKj+TMGu1L/JtdZ7AGg6gssxtJIRzrv8Busbwz8F9FLyikCcgG8gwToXw9cwRBR77S1m3mhJjrPsNnJ6X4DhIukknEg6jKEhToyJdigQQKBgQDp36oEM7yNQik9yGpjjt7DleDlEh9NQkFo1V3wkjUSORHJv1/Sj+1wCqwHerzRnUbLOxvryGufMQP0EuE4Z1aaghfMk4H/Qhs/Xx2TkfCPFf2O30Pb52uEHRjwR9De3Zylz5bvLU4k4Oq4RUxHI8Mn2XQiJf/x/MoTSAyzq2h4iQKBgQCmIeEBg8NFdjjLU4QIUeknOy6LwT1IIVFF/+vVylFzDLHxPdhcziL+aBpWhK1f+n5ZFFWcJ5DokSeHDZ8XNhxB/Va9wrhc+Gr9pyoMx/KRYS9iQ+3yY91QbdJvaYnzPW9CNAgQe0O38Yjr3a6bmwGPlV5nlAVsX228G1l+jzrxGQKBgQCSmcJgOfISdguWD+AoojEfVv8PeImoi0SnK0BkgbHEx4NP/KdbPR8wIXBui9B6cOUkldvNs+dKdZYt0/vbuGNAHRZABaLyWATud3f5vfyWJMm5WliWKv1pZjfwvoO+gBomkfbh3HtkgJyBf+btnQxDGXtDwKAslzIwXfSgV2z4SQKBgEBFVxEI8qCKzgoLM0csuTCOOsGibpzbFuAn1AsPjFcgEC8uJ+gvb2ZQy++wwIojFIJN1Zjlza/LiCk9YdDPBDubUNV04XJGQM4Ev8XWDMele3leok1lyRb2hbTIda/+km5gRsZpiZc+ACSPzJ8yZnYv6SfVIgv9Fyzwv7ywmLLxAoGAMkcRq786b4U13Obl+VudmxKKrN0n8sTCEGN2v76LL62tFrU1PJgmk1UR500NjgbPo4ET9gV9rDr0nDxdwr3h+mtDfhaKRCrJpJqtAjgMgGFlZP//ZLWsHtv8kTC5lACeEhGp9FoeWKEdXJpL6h0ZrT5sl440qFLy4RYP9gIiF4s=";

    //商户公钥；在网页端设置给支付宝

    //支付宝私钥

    // 支付宝公钥,支付宝自动返给我们。查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjDPXDdHR+Y8LoIBA4X+twduFYLrp+7aYojqqwMuLwM5jAiZyD7N3CtY0uA0DhDxYYsNPFmW2xAs1TVtYTw7e1zMLj4Hs7Sxaqe693LyymCKVupcQ0cOyRLA9pf1NoB1bZ2p/Uj5QT5rHB0lPdzb76/pX6HOLwhl7gXibo5veLMJFAztKmcblQ9xub7Heuz1NjKy0oN4TwcGOL2LCN9aMdC1irfgVEExSc4+ewUny9fnIRRvb4ydOfUWNOr7N6LiTze0B1R4QgbR3w4NYvSI1GL0p6hS7pjzJwiexFnVdOT25nUhc3vLCnz5sOLcV9m/74zdMhxKFa2pbfUC9LHGcDQIDAQAB";


    //支付成功以后：同步跳转地址；浏览器最后要跳到的页面
    //支付成功以后：通知地址：支付成功以后，支付宝会给我们指定的api接口，发送消息。判断支付宝这个消息才知道成功失败

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    private  String notify_url = "";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    private  String return_url = "http://gmall.com:8080/alipay_demo/alipay.trade.page.pay-JAVA-UTF-8/return_url.jsp";

    // 签名方式
    private  String sign_type = "RSA2";

    // 字符编码格式
    private  String charset = "utf-8";




}
