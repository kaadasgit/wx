package com.chason.wx.common.util;

import java.util.UUID;

public class UuidUtil {
    public static String randomUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String random32Key() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String random16Key() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
    }
}
