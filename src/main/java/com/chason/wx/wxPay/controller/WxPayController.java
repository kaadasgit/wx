package com.chason.wx.wxPay.controller;

import com.alibaba.fastjson.JSONObject;
import com.chason.wx.wxPay.service.WxPayService;
import com.chason.wx.wxPay.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@CrossOrigin
@RestController
@RequestMapping("/wxMp")
public class WxPayController {
    private static Logger logger = LoggerFactory.getLogger(WxPayController.class);

    @Autowired
    private WxPayService wxPayService;

    /**
     * 预支付下单
     *
     * @param orderNo
     * @param notifyUrl
     * @param openid
     * @param productName
     * @param orderAmount
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("unionOrder")
    public JSONObject unifiedorder(@RequestParam(required = true, value = "orderNo") String orderNo,
                                   @RequestParam(required = true, value = "notifyUrl") String notifyUrl,
                                   @RequestParam(required = true, value = "openid") String openid,
                                   @RequestParam(required = true, value = "productName") String productName,
                                   @RequestParam(required = true, value = "orderAmount") int orderAmount,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {

        String spbill_create_ip = WebUtil.getIpAddr(request);
        int index = spbill_create_ip.indexOf(",");
        if (index > 0) {
            spbill_create_ip = spbill_create_ip.substring(0, index);
        }

        JSONObject json = new JSONObject();
        json = wxPayService.commonPay(orderNo, notifyUrl, openid, productName, orderAmount, spbill_create_ip);
        return json;
    }

    @RequestMapping("paynotify")
    public void payNotity(HttpServletRequest request, HttpServletResponse response) {


        //
        String responseStr = wxPayService.payNotity(request);





    }

    /**
     * 向请求响应写消息
     *
     * @param response
     * @param message
     */
    protected void writeMessageToResponse(HttpServletResponse response, String message) {
        PrintWriter writer = null;
        try {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            writer = response.getWriter();
            writer.write(message);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}
