package cn.com.onlinetool.fastpay.pay.wxpay.enums;

/**
 * @author choice
 * @description: 自定义异常返回信息
 * @date 2019-06-12 10:57
 *
 */
public enum FastPayExceptionEnum {

    UNKNOW_ERROR(-1, "未知异常"),
    SUCCESS(0, "成功"),
    PARAM_ERROR(1, "参数错误"),
    CONFIG_ERROR(2, "配置错误, 请检查是否漏了配置项"),
    ALIPAY_NOTIFY_ID_VERIFY_FAIL(10, "【支付宝web端支付验证签名】验证notifyId失败"),
    ALIPAY_ASYNC_SIGN_VERIFY_FAIL(11, "【支付宝web端支付同步返回验证签名】验证签名失败"),
    SYNC_SIGN_VERIFY_FAIL(12, "同步返回签名失败"),
    ASYNC_SIGN_VERIFY_FAIL(13, "异步返回签名失败"),
    PAY_TYPE_ERROR(14, "错误的支付方式"),
    ALIPAY_TRADE_STATUS_IS_NOT_SUCCESS(15, "支付宝交易状态不是成功"),
    ALIPAY_TIME_FORMAT_ERROR(16, "支付宝返回的时间格式不对"),
    ;

    private Integer code;

    private String msg;

    FastPayExceptionEnum(Integer code, String msg) {
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
