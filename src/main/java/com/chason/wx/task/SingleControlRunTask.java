package com.chason.wx.task;

import com.chason.wx.wxmini.service.WxminiService;
import com.chason.wx.wxmp.service.WxmpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by chason on 2018/7/6.10:09
 */
@Component
public class SingleControlRunTask {
    private static final Logger logger = LoggerFactory.getLogger(SingleControlRunTask.class);

    private final static ScheduledExecutorService CLEAN_EXIST_CACHE_ONE_HOUR_EXEC = Executors.newSingleThreadScheduledExecutor();
    @Autowired
    private WxmpService wxmpService;
    @Autowired
    private WxminiService wxminiService;

    @PostConstruct
    public void init() {
        logger.info("---------刷新公众号参数---------");
        CLEAN_EXIST_CACHE_ONE_HOUR_EXEC.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    mpRefreshAccessTokenAndJsticket();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0L, 1L, TimeUnit.HOURS);
    }

    /**
     * 公众号
     * access_token、jsapi_ticket定时刷新任务
     */
    public void mpRefreshAccessTokenAndJsticket() {
        try {
            logger.info("公众号accesstoken定时任务");
            wxmpService.checkAccessTokenAndJsticket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 小程序
     */
    public void miniRefreshAccessToken() {
        try {
            logger.info("小程序accesstoken定时任务");
            wxminiService.refreshAccessToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
