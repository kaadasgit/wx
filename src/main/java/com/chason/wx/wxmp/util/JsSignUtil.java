package com.chason.wx.wxmp.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by chason on 2018/7/6.14:49
 */
public class JsSignUtil {
    public static String accessToken = null;

    public static Map<String, String> sign(String url,String appId,String jsapiTicket) throws IOException {

        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapiTicket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;
        System.out.println("string1="+string1);

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapiTicket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        ret.put("appId", appId);

        System.out.println("1.ticket(原始)="+jsapiTicket);
        System.out.println("2.url="+ret.get("url"));
        System.out.println("3.jsapi_ticket（处理后）="+ret.get("jsapi_ticket"));
        System.out.println("4.nonceStr="+ret.get("nonceStr"));
        System.out.println("5.signature="+ret.get("signature"));
        System.out.println("6.timestamp="+ret.get("timestamp"));

        return ret;
    }


    /**
     * 随机加密
     * @param hash
     * @return
     */
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    /**
     * 产生随机串--由程序自己随机产生
     * @return
     */
    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    /**
     * 由程序自己获取当前时间
     * @return
     */
    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

}
