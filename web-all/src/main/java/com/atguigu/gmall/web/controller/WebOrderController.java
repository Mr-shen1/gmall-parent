package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.client.order.OrderConfirmClient;
import com.atguigu.gmall.model.api.ConfirmOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/24
 */
@Controller
public class WebOrderController {

    @Autowired
    private OrderConfirmClient orderConfirmClient;


    @GetMapping("/trade.html")
    public String checkoutCart(ModelMap modelMap) {


        ConfirmOrderVo confirmOrderVo = orderConfirmClient.checkoutCart();


        modelMap.addAttribute("detailArrayList", confirmOrderVo.getDetailArrayList());
        modelMap.addAttribute("totalNum", confirmOrderVo.getTotalNum());
        modelMap.addAttribute("totalAmount", confirmOrderVo.getTotalAmount());
        modelMap.addAttribute("userAddressList", confirmOrderVo.getUserAddressList());
        modelMap.addAttribute("tradeNo", confirmOrderVo.getTradeNo());

        return "order/trade";

    }

    @GetMapping("/orderList.html")
    public String toOrderList() {
        return "order/myOrder";
    }
}
