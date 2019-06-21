package cn.com.onlinetool.fastpay.pay.wxpay.enums;


/**
 * @author choice
 * @description: 自定义异常处理
 * @date 2019-06-12 10:57
 *
 */
public enum  WXPayExceptionEnum {

    /**
     * 微信支付自定义异常
     */
    OPENID_NOT_EMPTY_FOR_JSAPI(970001, "JSAPI支付必须传openid"),
    ;

    private Integer code;

    private String msg;

    WXPayExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
