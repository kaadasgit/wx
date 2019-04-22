package com.chason.wx.wxPay.service;

import com.alibaba.fastjson.JSONObject;
import com.chason.wx.common.util.HttpUtil;
import com.chason.wx.common.util.Md5Util;
import com.chason.wx.common.util.UuidUtil;
import com.chason.wx.wxPay.entity.MpBaseResponse;
import com.chason.wx.wxPay.entity.MpPayNotifyRequest;
import com.chason.wx.wxPay.entity.MpUnifiedorderResponse;
import com.chason.wx.wxmp.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
public class WxPayService {
    private static Logger logger = LoggerFactory.getLogger(WxPayService.class);

    public static final String CODE = "code";
    protected static final int CODE_SUCC = 0;
    protected static final int CODE_ERROR = 1;
    protected static final int CODE_ALREADY_PRIZE = 2;

    private static final String tradeType = "JSAPI";// 交易类型
    private static final String PaySignType = "MD5";// 支付签名加密方式
    String appid = "";
    String MchId = "";
    String payKey = "";

    /**
     * 统一下单
     *
     * @param orderNo
     * @param notifyUrl
     * @param openid
     * @param productName
     * @param orderAmount
     * @param spbill_create_ip
     * @return
     */
    public JSONObject commonPay(String orderNo, String notifyUrl, String openid, String productName, int orderAmount, String spbill_create_ip) {
        JSONObject json = new JSONObject();

        MpUnifiedorderResponse unifiedorderResponse = unifiedorder(openid, appid, MchId, productName, orderNo, orderAmount, spbill_create_ip, notifyUrl,
                tradeType, null, payKey);
        if (null != unifiedorderResponse && StringUtils.equalsIgnoreCase(unifiedorderResponse.getReturn_code(), MpUnifiedorderResponse.SUCC)
                && StringUtils.equalsIgnoreCase(unifiedorderResponse.getResult_code(), MpUnifiedorderResponse.SUCC)) {
            json.put(CODE, CODE_SUCC);
            json.put("appid", appid);
            json.put("prepay_id", unifiedorderResponse.getPrepay_id());// 微信预支付id
            String nonce_str = UuidUtil.randomUUID();
            String time_stamp = String.valueOf(System.currentTimeMillis());
            String paySign = "appId=" + appid + "&nonceStr=" + nonce_str + "&package=prepay_id=" + unifiedorderResponse.getPrepay_id() + "&signType=" + PaySignType
                    + "&timeStamp=" + time_stamp + "&key=" + payKey;
            String md5SignValue = Md5Util.getMD5(paySign.getBytes()).toUpperCase();// 计算支付签名
            json.put("nonce_str", nonce_str);
            json.put("time_stamp", time_stamp);
            json.put("sign_type", PaySignType);
            json.put("paySign", md5SignValue);
            json.put("out_trade_no", orderNo);// 系统订单编号
            return json;
        } else {
            json.put(CODE, CODE_ERROR);
            return json;
        }
    }

    /**
     * 微信预下单(xml格式数据转换)
     *
     * @param openid
     * @param appid
     * @param mch_id
     * @param body
     * @param out_trade_no
     * @param total_fee
     * @param spbill_create_ip
     * @param notify_url
     * @param trade_type
     * @param product_id
     * @param key
     * @return
     */
    public MpUnifiedorderResponse unifiedorder(String openid, String appid, String mch_id, String body, String out_trade_no, int total_fee, String spbill_create_ip,
                                               String notify_url, String trade_type, String product_id, String key) {
        if (StringUtils.isEmpty(appid) || StringUtils.isEmpty(mch_id) || StringUtils.isEmpty(body) || StringUtils.isEmpty(out_trade_no) || StringUtils.isEmpty(spbill_create_ip)
                || StringUtils.isEmpty(notify_url) || StringUtils.isEmpty(trade_type) || StringUtils.isEmpty(key))
            return null;
        if (StringUtils.equalsIgnoreCase(MpBaseResponse.JSAPI, trade_type) && StringUtils.isEmpty(openid))
            return null;
        else if (StringUtils.equalsIgnoreCase(MpBaseResponse.NATIVE, trade_type) && StringUtils.isEmpty(product_id))
            return null;

        body = StringUtils.substring(body, 0, 40);

        String nonce_str = UuidUtil.randomUUID();
        Document document = DocumentHelper.createDocument();
        Element element = document.addElement("xml");
        element.addElement("appid").setText(appid);
        element.addElement("mch_id").setText(mch_id);
        element.addElement("nonce_str").setText(nonce_str);
        // element.addElement("body").setText(body);
        element.addElement("body").addCDATA(body);
        element.addElement("out_trade_no").setText(out_trade_no);
        element.addElement("total_fee").setText(String.valueOf(total_fee));
        element.addElement("spbill_create_ip").setText(spbill_create_ip);
        element.addElement("notify_url").setText(notify_url);
        element.addElement("trade_type").setText(trade_type);
        element.addElement("openid").setText(openid);

        // 对参数按照key=value的格式，并按照参数名ASCII字典序排序
        String signStr = "appid=" + appid + "&body=" + body + "&mch_id=" + mch_id + "&nonce_str=" + nonce_str + "&notify_url=" + notify_url + "&openid=" + openid
                + "&out_trade_no=" + out_trade_no + (StringUtils.equalsIgnoreCase(MpBaseResponse.NATIVE, trade_type) ? ("&product_id=" + product_id) : "") + "&spbill_create_ip="
                + spbill_create_ip + "&total_fee=" + total_fee + "&trade_type=" + trade_type + "&key=" + key;

        String sign = Md5Util.getMD5(signStr.getBytes()).toUpperCase();

        // element.addElement("sign").setText(sign);
        element.addElement("sign").addCDATA(sign);
        String reqBody = document.asXML();
        String response = HttpUtil.sendHttpPostReqToServerByReqbody(Constants.MP_PAY_COMMON_PAY_URL, reqBody, null);
        logger.info("调用统一支付接口返回：" + response);
        if (StringUtils.isEmpty(response))
            return null;
        try {
            MpUnifiedorderResponse unifiedorderResponse = new MpUnifiedorderResponse();
            Document documentResp = DocumentHelper.parseText(response);
            Element root = documentResp.getRootElement();
            String return_code = root.elementText("return_code");
            unifiedorderResponse.setReturn_code(return_code);
            if (StringUtils.isEmpty(return_code) || StringUtils.equalsIgnoreCase(return_code, MpUnifiedorderResponse.FAIL)) {
                unifiedorderResponse.setReturn_msg(root.elementText("return_msg"));
                return unifiedorderResponse;
            }
            unifiedorderResponse.setAppid(root.elementText("appid"));
            unifiedorderResponse.setMch_id(root.elementText("mch_id"));
            unifiedorderResponse.setDevice_info(root.elementText("device_info"));
            unifiedorderResponse.setNonce_str(root.elementText("nonce_str"));
            unifiedorderResponse.setSign(root.elementText("sign"));
            String result_code = root.elementText("result_code");
            unifiedorderResponse.setResult_code(result_code);
            if (StringUtils.isEmpty(result_code) || StringUtils.equalsIgnoreCase(result_code, MpUnifiedorderResponse.FAIL)) {
                return unifiedorderResponse;
            }
            unifiedorderResponse.setTrade_type(root.elementText("trade_type"));
            unifiedorderResponse.setPrepay_id(root.elementText("prepay_id"));
            unifiedorderResponse.setCode_url(root.elementText("code_url"));
            return unifiedorderResponse;
        } catch (DocumentException e) {
            logger.error("解析调用统一支付接口xml错误", e);
        }
        return null;
    }

    /**
     * 支付回调处理
     *
     * @param request
     * @return
     */
    public String payNotity(HttpServletRequest request) {
        MpPayNotifyRequest notify = addPayNotifyRequest(request);
        logger.info("=======收到微信支付通知,充值订单编号=========" + notify.getOut_trade_no());
        if (StringUtils.equalsIgnoreCase(notify.getReturn_code(), MpPayNotifyRequest.SUCC) && StringUtils.equalsIgnoreCase(notify.getResult_code(), MpPayNotifyRequest.SUCC)) {
            // 乐观锁版本号控制并发通知重复充值
            String appId = notify.getAppid();
            String tjOpenId = notify.getOpenid();

            //
            if (StringUtils.isNotEmpty(appId) && StringUtils.isNotEmpty(tjOpenId)) {
                String responseStr = getMpNotifyResponseStr();
                return responseStr;
            }
        }

        return null;
    }

    /**
     * 支付回调内容解析
     *
     * @param request
     * @return
     */
    public MpPayNotifyRequest addPayNotifyRequest(HttpServletRequest request) {
        StringBuffer reqContent = new StringBuffer();
        try {
            request.setCharacterEncoding(StandardCharsets.UTF_8.name());
            BufferedReader read = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8.toString()));
            String line = null;
            while ((line = read.readLine()) != null) {
                reqContent.append(line);
            }
            read.close();
            if (StringUtils.isEmpty(reqContent.toString()))
                return null;
            Document document = DocumentHelper.parseText(reqContent.toString());
            Element root = document.getRootElement();
            MpPayNotifyRequest response = new MpPayNotifyRequest();
            response.setReturn_code(root.elementText("return_code"));
            response.setReturn_msg(root.elementText("return_msg"));
            response.setAppid(root.elementText("appid"));
            response.setMch_id(root.elementText("mch_id"));
            response.setDevice_info(root.elementText("device_info"));
            response.setNonce_str(root.elementText("nonce_str"));
            response.setSign(root.elementText("sign"));
            response.setResult_code(root.elementText("result_code"));
            response.setErr_code(root.elementText("err_code"));
            response.setErr_code_des(root.elementText("err_code_des"));
            response.setOpenid(root.elementText("openid"));
            response.setIs_subscribe(root.elementText("is_subscribe"));
            response.setTrade_type(root.elementText("trade_type"));
            response.setBank_type(root.elementText("bank_type"));
            int total_fee = Integer.valueOf(StringUtils.isEmpty(root.elementText("total_fee")) ? "0" : root.elementText("total_fee"));
            int coupon_fee = Integer.valueOf(StringUtils.isEmpty(root.elementText("coupon_fee")) ? "0" : root.elementText("coupon_fee"));
            response.setTotal_fee(total_fee);
            response.setCoupon_fee(coupon_fee);
            response.setFee_type(root.elementText("fee_type"));
            response.setTransaction_id(root.elementText("transaction_id"));
            response.setOut_trade_no(root.elementText("out_trade_no"));
            response.setAttach(root.elementText("attach"));
            response.setTime_end(root.elementText("time_end"));
            return response;
        } catch (Exception e) {
            logger.error("parseXml-Pay-Notify", e);
        }
        return null;
    }

    /**
     * 支付回调成功应答内容封装
     *
     * @return
     */
    private String getMpNotifyResponseStr() {
        Document document = DocumentHelper.createDocument();
        Element element = document.addElement("xml");
        element.addElement("return_code").setText("SUCCESS");
        element.addElement("return_msg").setText("OK");
        return document.asXML();
    }
}
