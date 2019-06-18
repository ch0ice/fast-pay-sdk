package cn.com.onlinetool.fastpay.pay.wxpay.enums;

/**
 * @author choice
 * 支付方式
 * @date 2019-06-18 10:55
 *
 */
public enum WXPayTypeEnum {

    /**
     * 支付方式
     */
    MICROPAY("MICROPAY", "付款码支付"),
    JSAPI("JSAPI", "JSAPI支付"),
    SMALL("SMALL", "小程序支付"),
    NATIVE("NATIVE", "Native支付/扫码支付"),
    APP("APP", "APP支付"),
    H5("H5", "H5支付/扫码支付"),
    ;

    private String type;

    private String desc;


    WXPayTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
