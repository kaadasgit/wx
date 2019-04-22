package com.chason.wx.wxmp.controller;

import com.alibaba.fastjson.JSONObject;
import com.chason.wx.common.Result;
import com.chason.wx.wxmp.service.WxmpService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;


/**
 * @author by wnf
 * @create 2018/6/19
 */
@CrossOrigin
@Controller
@RequestMapping("/wxMp")
public class WxMpController {
    private static Logger logger = LoggerFactory.getLogger(WxMpController.class);

    @Autowired
    private WxmpService wxmpService;
    @Value("${wxmp.redirectUri}")
    private String redirectUri;


    /**
     * 授权
     *
     * @param returnUrl
     * @return
     */
    @GetMapping(value = "/oauth")
    public String oauth(@RequestParam(value = "returnUrl") String returnUrl) {
        // 直接调工具包封装好的授权方法
        String url = redirectUri + "?callBackPage=" + returnUrl;
        // 授权后的重定向的回调链接地址进行URLEncode编码处理,这里的scope= snsapi_userinfo
        String redirectUrl = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect", "wxeac5fe16460f775c", URLEncoder.encode(url), "snsapi_userinfo", "state");
        return "redirect:" + redirectUrl;
    }

    /**
     * 授权获取用户信息
     *
     * @param code
     * @param response
     */
    @RequestMapping("auth_userinfo")
    @ResponseBody
    public String auth_userinfo(@RequestParam(value = "code", required = true) String code,
                              @RequestParam(value = "state", required = false) String state,
                              @RequestParam(value = "callbackUrl", required = false) String callbackUrl,
                              HttpServletResponse response) {
        String openId = "";
        String headimgUrl = "";
        String nickName = "";
        String unionId = "";
        JSONObject userInfo = wxmpService.getSnsUserInfoByOauthCode(code);
        if (userInfo != null && !userInfo.containsKey("errcode")) {
            openId = userInfo.containsKey("openid") ? userInfo.getString("openid") : "";
            headimgUrl = userInfo.containsKey("headimgurl") ? userInfo.getString("headimgurl") : "";
            nickName = userInfo.containsKey("nickname") ? userInfo.getString("nickname") : "";
            unionId = userInfo.containsKey("unionid") ? userInfo.getString("unionid") : "";
        }

//        StringBuilder sb = new StringBuilder();
//        sb.append(callbackUrl);
//        sb.append("?qfOpenid=");
//        sb.append(openId);
//        sb.append("&headimgurl=");
//        sb.append(headimgUrl);
//        sb.append("&nickname=");
//        sb.append(nickName);
//        sb.append("&unionid=");
//        sb.append(unionId);
//        try {
//            String returnUrl = sb.toString();
//            if (!returnUrl.startsWith("http://") && !returnUrl.startsWith("https://")) {
//                returnUrl = "http://" + returnUrl;
//            }
//            response.sendRedirect(returnUrl);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return userInfo.toString();
    }

    @GetMapping("getAccessToken")
    public Result getAccessToken(@RequestParam(value = "appId", required = true) String appId) throws Exception {
        String accessToken = wxmpService.getAccessToken();
        if (null == accessToken) {
            return Result.error("查询失败");
        } else {
            return Result.success(accessToken, "查询成功");
        }
    }

    @GetMapping("getJsTicket")
    public Result getJsTicket(@RequestParam(value = "appId", required = true) String appId) throws Exception {
        String jsTicket = wxmpService.getJsTicket();
        if (null == jsTicket) {
            return Result.error("查询失败");
        } else {
            return Result.success(jsTicket, "查询成功");
        }
    }

    @GetMapping("isAttention")
    public Result isAttention(@RequestParam(value = "appId", required = true) String appId,
                              @RequestParam(value = "openId", required = true) String openId) throws Exception {
        String result = wxmpService.isAttention(appId, openId);
        if (StringUtils.isEmpty(result)) {
            return Result.error("查询失败");
        } else {
            return Result.success(JSONObject.parse(result), "查询成功");
        }
    }
}
