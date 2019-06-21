package cn.com.onlinetool.fastpay.pay.wxpay.constants;

/**
 * @author choice
 * 支付方式
 * JSAPI--JSAPI支付（或小程序支付）、
 * NATIVE--Native支付、
 * APP--app支付，
 * MWEB--H5支付，
 * 不同trade_type决定了调起支付的方式，请根据支付产品正确上传
 * @date 2019-06-18 10:55
 */
public final class WXPayTypeConstants {

    /**
     * JSAPI支付/小程序支付
     */
    public static final String JSAPI = "JSAPI";
    /**
     * NATIVE支付/扫码支付
     */
    public static final String NATIVE = "JSAPI";
    /**
     * APP支付
     */
    public static final String APP = "APP";
    /**
     * H5支付
     */
    public static final String H5 = "MWEB";
}
