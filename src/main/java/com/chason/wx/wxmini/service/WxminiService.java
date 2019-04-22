package com.chason.wx.wxmini.service;

import com.alibaba.fastjson.JSONObject;
import com.chason.wx.common.util.HttpUtil;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;

@Service
public class WxminiService {
    private static final Logger logger = LoggerFactory.getLogger(WxminiService.class);

    private String accessToken = "";

    @Value("${wxmini.jscode2SessionURL}")
    private String jscode2SessionURL;
    @Value("${wxmini.accessTokenURL}")
    private String accessTokenURL;
    @Value("${wxmini.appid}")
    private String appid;
    @Value("${wxmini.secret}")
    private String secret;

    /**
     * 获取小程序的openid
     *
     * @return
     * @throws Exception
     */
    public String login(String jsCode, String rawData, String signature, String encrypteData, String iv) throws Exception {
        logger.info("Start get SessionKey");
        // 非敏感信息
        JSONObject rawDataJson = null;
        if (!StringUtils.isEmpty(rawData)) {
            rawDataJson = JSONObject.parseObject(rawData);
        }

        String url = String.format(jscode2SessionURL, appid, secret, jsCode);
        String data = HttpUtil.sendHttpGetReqToServer(url);
        JSONObject jsonObject = JSONObject.parseObject(data);
        if (jsonObject.containsKey("errcode") && 0 != jsonObject.getInteger("errcode")) {
            logger.error("小程序授权失败：{}", jsonObject.getString("errmsg"));
            return "小程序授权失败";
        }

        String openid = jsonObject.getString("openid");
        String sessionKey = jsonObject.getString("session_key");
        // 将session保存在redis
//        redisService.set(RedisKeyUtil.getWeixinSessionKey(openid), sessionKey, 300L);

        // 判定数据库中是否存在，不存在就插入

        JSONObject json = new JSONObject();
        json.put("openid", openid);
        if (!StringUtils.isEmpty(encrypteData) && !StringUtils.isEmpty(iv)) {
            JSONObject userInfo = getUserInfo(encrypteData, sessionKey, iv);
            System.out.println("根据解密算法获取的userInfo=" + userInfo);
            json.put("userInfo", userInfo);
        }
        return json.toString();
    }

    /**
     * 获取小程序accessToken
     *
     * @return
     * @throws Exception
     */
    public String getWxminiAccessToken(String appid) throws Exception {
        if (StringUtils.isEmpty(appid)) {
            return null;
        }
        return accessToken;
    }

    /**
     * 定时刷新小程序accessToken
     */
    public void refreshAccessToken() {
        String resp = HttpUtil.sendHttpGetReqToServer(String.format(accessTokenURL, appid, secret));
        logger.info("---- get accessToken, appid = " + appid + ", resp = " + resp);

        if (StringUtils.isEmpty(resp))
            return;

        JSONObject respJson = JSONObject.parseObject(resp);
        if (null == respJson)
            return;

        if (0 != respJson.getInteger("errcode"))
            return;

        String accessToken = respJson.getString("access_token");

        try {
            accessToken = accessToken;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static JSONObject getUserInfo(String encryptedData, String sessionKey, String iv) {
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSONObject.parseObject(result);
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
        } catch (NoSuchPaddingException e) {
            logger.error(e.getMessage(), e);
        } catch (InvalidParameterSpecException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalBlockSizeException e) {
            logger.error(e.getMessage(), e);
        } catch (BadPaddingException e) {
            logger.error(e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            logger.error(e.getMessage(), e);
        } catch (InvalidAlgorithmParameterException e) {
            logger.error(e.getMessage(), e);
        } catch (NoSuchProviderException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
