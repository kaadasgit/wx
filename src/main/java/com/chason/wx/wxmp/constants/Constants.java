package com.chason.wx.wxmp.constants;

/**
 * @author by wnf
 * @create 2018/6/19
 */
public class Constants {
    // Session中的键常量
    public static final String SESSION_MANAGER = "session_manager";

    public static final String COOKIE_WEB_USERNAME = "cookie_web_username";
    public static final String COOKIE_WEB_PASSWORD = "cookie_web_password";

    public static final String COOKIE_APPID = "cookie_appId";
    public static final String COOKIE_OPENID = "cookie_openId";
    public static final String COOKIE_ACCESSTOKEN = "cookie_accessToken";

    // 微信公众号自定义菜单创建消息体类型JSON
    public static final String MP_WEIXIN_MENU_BODY_TYPE = "application/json";

    // 微信公众号自定义菜单查询接口
    public static final String MP_WEIXIN_MENU_QUERY_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=%s";

    // 微信公众号自定义菜单创建接口
    public static final String MP_WEIXIN_MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";

    // 微信公众号自定义菜单删除接口
    public static final String MP_WEIXIN_MENU_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=%s";

    public static String SERVLET_CONTEXT = "";
    /*********************************************
     * 公用部分相关
     *********************************************/
    public static final int API_REQUEST_SUCCESS = 0;// API接口调用成功返回的状态码
    public static final String SYSTEM_SUCCESS = "0";
    public static final String SYSTEM_ERROR = "-1";
    public static final String PARAMS_UNVALID = "-2";
    public static final String EMPTY_RESULT = "-3";
    public static final String AUTHORIZATION_UNVALID = "77";// "授权信息不存在"
    /*********************************************
     * 微信平台相关
     *********************************************/
    // 二维码有效时间,以秒为单位。 最大不超过1800
    public static final int DEFAULT_EXPIRE_SECONDS = 1800;
    public static final String QR_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s";
    public static final String DOWN_QRCODE_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s";
    // expire_seconds 该二维码有效时间，以秒为单位。 最大不超过1800。 action_name
    // 二维码类型，QR_SCENE为临时,QR_LIMIT_SCENE为永久 action_info 二维码详细信息
    // scene_id场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
    public static final String TEMP_QRCODE_JSON = "{\"expire_seconds\": {expire_seconds}, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": {scene_id}}}}";
    public static final String FOREVER_QRCODE_JSON = "{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": {scene_id}}}}";
    // 微信公众平台API通行证ACCESS_TOKEN地址
    public static final String GET_WEIXIN_API_ACCESS_TOKEN_REQ_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    // 微信公众平台网页获取accesstoken地址,页面微信授权AccessToken业务处理服务接口，该AccessToken与基础接口的AccessToken不一样，详见微信公众平台详细文档：http://mp.weixin.qq.com/wiki/index.php?title=网页授权获取用户基本信息
    public static final String GET_WEIXIN_SNS_ACCESS_TOKEN_REQ_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    // 微信公众平台发货通知时的加密签名模板
    public static final String WEI_XIN_PAY_DELVER_NOTIFY_SIGNATURE_TEMPLATE = "appid=%s&appkey=%s&deliver_msg=%s&deliver_status=%s&deliver_timestamp=%s&openid=%s&out_trade_no=%s&transid=%s";
    // 微信公众平台发货通知地址模板
    public static final String WEI_XIN_PAY_DELVER_NOTIFY_URL = "https://api.weixin.qq.com/pay/delivernotify?access_token=%s";

    // 上传文件获取media_id
    public final static String UPLOAD_URL = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=%s&type=%s";
    // 发送客服消息
    public final static String SERVICE_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";
    // 发送客服消息
    public final static String GET_SNS_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
    // 发送模板消息
    public final static String SEND_TEMPLATE_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
    // 获取用户信息
    public final static String GET_USERINFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";

    // snsapi_userinfo
    // （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
    public static final String OAUTH2_AUTHORIZE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect";
    // 获取微信JS-SDK里面的jsapi_ticket
    public static final String GET_WEIXIN_JSAPI_TICKET_REQ_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";

    // 统一支付接口
    public static final String MP_PAY_COMMON_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    // 订单查询接口
    public static final String MP_PAY_ORDER_QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
    // 退款申请接口
    public static final String MP_PAY_ORDER_REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    // 退款查询接口
    public static final String MP_PAY_ORDER_REFUND_QUERY_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
    // 卡券 上传商户logo
    public static final String CARD_UPLOADIMG = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=%s";
    // 卡券 获取卡券颜色
    public static final String CARD_GET_COLORS_URL = "https://api.weixin.qq.com/card/getcolors?access_token=%s";
    // 卡券 创建
    public static final String CARD_CREATE_URL = "https://api.weixin.qq.com/card/create?access_token=%s";
    // 卡券 二维码生成
    public static final String CARD_QRCODE_CREATE_URL = "https://api.weixin.qq.com/card/qrcode/create?access_token=%s";
    // 卡券 核销
    public static final String CARD_CONSUME_URL = "https://api.weixin.qq.com/card/code/consume?access_token=%s";
    // 卡券 解码
    public static final String CARD_DECRYPT_URL = "https://api.weixin.qq.com/card/code/decrypt?access_token=%s";
    // 卡券 删除
    public static final String CARD_DELETE_URL = "https://api.weixin.qq.com/card/delete?access_token=%s";
    // 卡券 查询卡号资料
    public static final String CARD_CODE_GET_URL = "https://api.weixin.qq.com/card/code/get?access_token=%s";
    // 卡券 批量查询卡券ID
    public static final String CARD_BATCH_GET_URL = "https://api.weixin.qq.com/card/batchget?access_token=%s";
    // 卡券 查询卡券信息
    public static final String CARD_INFO_GET_URL = "https://api.weixin.qq.com/card/get?access_token=%s";
    // 卡券 更新卡号
    public static final String CARD_UPDATE_URL = "https://api.weixin.qq.com/card/code/update?access_token=%s";
    // 卡券 作废
    public static final String CARD_UNAVAILABLE_URL = "https://api.weixin.qq.com/card/code/unavailable?access_token=%s";
    // 卡券 库存修改
    public static final String CARD_MODIFY_STOCK_URL = "https://api.weixin.qq.com/card/modifystock?access_token=%s";

    // 永久素材上传
    public static final String FOREVER_MATERIAL_ADD_URL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=%s&type=%s";
    // 永久素材删除
    public static final String FOREVER_MATERIAL_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/material/del_material?access_token=%s";
    // 图文素材上传
    public static final String FOREVER_NEWS_ADD_URL = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=%s";
    // 图文素材获取
    public static final String FOREVER_NEWS_GET_URL = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=%s";
    // 图文素材更新
    public static final String FOREVER_NEWS_UPDATE_URL = "https://api.weixin.qq.com/cgi-bin/material/update_news?access_token=%s";
    // 推送图文消息
    public static final String FOREVER_NEWS_PUSH_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=%s";

    // 消息类型
    public static final String MSGTYPE_TEXT = "text";
    public static final String MSGTYPE_IMAGE = "image";
    public static final String MSGTYPE_VOICE = "voice";
    public static final String MSGTYPE_VIDEO = "video";
    public static final String MSGTYPE_NEWS = "news";
    public static final String MSGTYPE_PAGES = "pages";
    public static final String MSGTYPE_MUSIC = "music";
    public static final String MSGTYPE_EVENT = "event";
    public static final String MSGTYPE_LINK = "link";
    public static final String MSGTYPE_LOCATION = "location";

    public static final String TYPE = "type";
    public static final String MEDIA_ID = "media_id";


    /**
     * 微信推送消息相关的KEY
     */
    public static final String TO_USER_NAME = "ToUserName";
    public static final String FROM_USER_NAME = "FromUserName";
    public static final String CREATE_TIME = "CreateTime";
    public static final String MSG_TYPE = "MsgType";
    public static final String EVENT = "Event";
    public static final String CONTENT = "Content";
    public static final String MSG_ID = "MsgID";
    public static final String STATUS = "Status";
    public static final String TOTAL_COUNT = "TotalCount";
    public static final String FILTER_COUNT = "FilterCount";
    public static final String SENT_COUNT = "SentCount";
    public static final String ERROR_COUNT = "ErrorCount";



    // 是否关注公众号
    public final static String ISATTENTION_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s";
}
