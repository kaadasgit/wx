package com.chason.wx.wxmp.service;

import com.alibaba.fastjson.JSONObject;
import com.chason.wx.common.util.HttpUtil;
import com.chason.wx.wxmp.constants.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WxmpService {
    private static Logger logger = LoggerFactory.getLogger(WxmpService.class);
    public static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String WEI_XIN_TV_GET_ACCESSTOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";// 获取微信JS-SDK里面的jsapi_ticket
    // 微信公众平台网页获取accesstoken地址,页面微信授权AccessToken业务处理服务接口，该AccessToken与基础接口的AccessToken不一样，详见微信公众平台详细文档：http://mp.weixin.qq.com/wiki/index.php?title=网页授权获取用户基本信息
    private static final String GET_WEIXIN_JSAPI_TICKET_REQ_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=%s";
    private static final String KEY_ERRCODE = "errcode";
    private static final String KEY_ACCESS_TOKEN = "access_token";


    private String accessToken;
    private String jsTicket;

//----------------------------------------------------------get、set---------------------------------------------------------------------

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getJsTicket() {
        return jsTicket;
    }

    public void setJsTiket(String jsTicket) {
        this.jsTicket = jsTicket;
    }

    //----------------------------------------------------------get、set---------------------------------------------------------------------

    @Value("${wxmp.appid}")
    private String appId;

    @Value("${wxmp.secret}")
    private String secret;


//----------------------------------------------------------微信授权 start---------------------------------------------------------------------

    /**
     * 用户授权
     * @param code
     * @return
     */
    public JSONObject getSnsUserInfoByOauthCode(String code) {
        JSONObject json = getWeixinSnsAccesstoken(code);
        String accessToken = null;
        String openid = null;
        if (null != json && !json.containsKey("errcode")) {
            accessToken = json.containsKey("access_token") ? json.get("access_token").toString() : "";
            openid = json.containsKey("openid") ? json.get("openid").toString() : "";
            return getSnsUserInfo(accessToken, openid);
        } else
            return json;
    }

    /**
     * 网页授权获取accessToken
     * @param code
     * @return
     */
    public JSONObject getWeixinSnsAccesstoken(String code) {
        if (StringUtils.isEmpty(code))
            return new JSONObject();
        String reqUrl = String.format(Constants.GET_WEIXIN_SNS_ACCESS_TOKEN_REQ_URL, appId, secret, code);
        String responseContent = HttpUtil.sendHttpGetReqToServerByHighPerformance(reqUrl);
        if (StringUtils.isEmpty(responseContent))
            return new JSONObject();
        return JSONObject.parseObject(responseContent);
    }

    /**
     * 网页授权获取用户
     *
     * @param accessToken
     * @param openid
     * @return
     */
    public JSONObject getSnsUserInfo(String accessToken, String openid) {
        if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(openid))
            return null;
        String reqUrl = String.format(Constants.GET_SNS_USERINFO_URL, accessToken, openid);
        String responseContent = HttpUtil.sendHttpGetReqToServer(reqUrl);
        if (StringUtils.isEmpty(responseContent))
            return null;
        return JSONObject.parseObject(responseContent);
    }
//----------------------------------------------------------微信授权 end---------------------------------------------------------------------

//----------------------------------------------------------微信Attention start---------------------------------------------------------------------
    public String isAttention(String appId, String openId) throws Exception {
        if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(openId)) {
            return null;
        }

        if (StringUtils.isEmpty(accessToken)) {
            return null;
        }

        return HttpUtil.sendHttpGetReqToServer(String.format(Constants.ISATTENTION_URL, openId));
    }
//----------------------------------------------------------微信Attention end---------------------------------------------------------------------

//----------------------------------------------------------刷新公众号accessToken、jsTicket、jsSign start---------------------------------------------------------------------

    public void checkAccessTokenAndJsticket() {
        logger.info("--------- 定时任务刷新所有公众号的accessToken、jsTicket ---------");
        String accesstoken = refreshAccessToken(appId, secret);
        refreshJsTicket(accesstoken, appId, secret, "jsapi");
    }

    //----------------------------------------------------------微信AccessToken start---------------------------------------------------------------------
    private String refreshAccessToken(String appid, String appSecret) {
        String resp = HttpUtil.sendHttpGetReqToServer(String.format(WEI_XIN_TV_GET_ACCESSTOKEN_URL, appid, appSecret));
        logger.info("---- get accessToken, appid = " + appid + ", resp = " + resp);

        if (StringUtils.isEmpty(resp))
            return null;

        JSONObject respJson = JSONObject.parseObject(resp);
        if (null == respJson)
            return null;

        if (respJson.containsKey(KEY_ERRCODE))
            return null;

        String wxAccessToken = respJson.getString(KEY_ACCESS_TOKEN);

        accessToken = wxAccessToken;

        return accessToken;
    }
    //----------------------------------------------------------微信AccessToken end---------------------------------------------------------------------

    //----------------------------------------------------------微信JsTicket start---------------------------------------------------------------------
    private void refreshJsTicket(String accesstoken, String appid, String secret, String ticketType) {

        if (StringUtils.isEmpty(accesstoken))
            return;

        String reqUrl = String.format(GET_WEIXIN_JSAPI_TICKET_REQ_URL, accesstoken, ticketType);
        String resp = HttpUtil.sendHttpGetReqToServerByHighPerformance(reqUrl);

        logger.info("---- get jsapiticket, appId = " + appid + ", ticketType = " + ticketType + ", resp " + resp);

        if (StringUtils.isEmpty(resp))
            return;

        JSONObject respJson = JSONObject.parseObject(resp);
        if (null == respJson || respJson.getInteger("errcode") != 0)
            return;

        jsTicket = respJson.getString("ticket");
    }
    //----------------------------------------------------------微信JsTicket end---------------------------------------------------------------------

//----------------------------------------------------------刷新公众号accessToken、jsTicket、jsSign start---------------------------------------------------------------------
}
