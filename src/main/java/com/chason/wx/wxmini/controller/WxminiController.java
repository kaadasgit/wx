package com.chason.wx.wxmini.controller;

import com.chason.wx.wxmini.service.WxminiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("wxmini")
public class WxminiController {
    @Autowired
    private WxminiService wxminiService;

    @RequestMapping("login")
    public String getOpenId(@RequestParam(value = "jsCode", required = true) String jsCode,
                            @RequestParam(value = "rawData", required = false) String rawData,
                            @RequestParam(value = "signature", required = false) String signature,
                            @RequestParam(value = "encrypteData", required = false) String encrypteData,
                            @RequestParam(value = "iv", required = false) String iv) throws Exception {
        return wxminiService.login(jsCode, rawData, signature, encrypteData, iv);
    }

    @RequestMapping("getWxminiAccessToken")
    public String getWxminiAccessToken(@RequestParam(value = "appid", required = true) String appid) throws Exception {
        String wxminiAcessToken = wxminiService.getWxminiAccessToken(appid);
        if (StringUtils.isEmpty(wxminiAcessToken)) {
            return "查询失败";
        } else {
            return "acessToken:" + wxminiAcessToken;
        }
    }
}
