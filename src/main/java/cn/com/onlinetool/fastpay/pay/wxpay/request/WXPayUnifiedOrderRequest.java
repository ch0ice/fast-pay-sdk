package cn.com.onlinetool.fastpay.pay.wxpay.request;

import cn.com.onlinetool.fastpay.annotations.validation.NotEmpty;
import cn.com.onlinetool.fastpay.annotations.validation.Validation;
import cn.com.onlinetool.fastpay.constants.CurrencyTypeConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author choice
 * @description: 微信 统一下单 请求对象
 * 应用场景：
 *      商户在小程序中先调用该接口在微信支付服务后台生成预支付交易单，返回正确的预支付交易后调起支付。
 * 是否需要证书：
 *      否
 * @date 2019-06-05 16:20
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Validation
public class WXPayUnifiedOrderRequest extends WXPayBaseRequest{

    /**
     * 商品描述 必填
     * 商品简单描述，该字段请按照规范传递
     */
    @NotEmpty
    private String body;

    /**
     * 商品详情 非必填
     * 商品详细描述，对于使用单品优惠的商户，该字段必须按照规范上传
     */
    private String detail;

    /**
     * 附加数据 非必填
     * 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。
     */
    private String attach;

    /**
     * 商户订单号 必填
     * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一。
     */
    @NotEmpty
    private String outTradeNo;

    /**
     * 标价币种 非必填
     * 符合ISO 4217标准的三位字母代码，默认人民币：CNY
     */
    private String feeType = CurrencyTypeConstants.CNY;

    /**
     * 标价金额 必填
     * 订单总金额，单位为分
     */
    @NotEmpty
    private Integer totalFee;

    /**
     * 终端IP 必填
     * 支持IPV4和IPV6两种格式的IP地址。调用微信支付API的机器IP
     */
    @NotEmpty
    private String spbillCreateIp;

    /**
     * 交易起始时间 非必填
     * 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010
     */
    private String timeStart;

    /**
     * 交易结束时间 非必填
     * 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。
     * 订单失效时间是针对订单号而言的，由于在请求支付的时候有一个必传参数prepay_id只有两小时的有效期，所以在重入时间超过2小时的时候需要重新请求下单接口获取新的prepay_id。
     */
    private String timeExpire;

    /**
     * 订单优惠标记 非必填
     * 订单优惠标记，使用代金券或立减优惠功能时需要的参数
     */
    private String goodsTag;

    /**
     * 通知地址 必填
     * 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
     */
    @NotEmpty
    private String notifyUrl;

    /**
     * 交易类型 必填
     * JSAPI--JSAPI支付（或小程序支付）、NATIVE--Native支付、APP--app支付，MWEB--H5支付，不同trade_type决定了调起支付的方式
     */
    @NotEmpty
    private String tradeType;

    /**
     * 商品ID 非必填
     * trade_type=NATIVE时，此参数必传。此参数为二维码中包含的商品ID，商户自行定义。
     */
    private String productId;

    /**
     * 指定支付方式 非必填
     * 上传此参数no_credit--可限制用户不能使用信用卡支付
     */
    private String limitPay;

    /**
     * 用户标识 非必填
     * trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。
     */
    private String openid;

    /**
     * 电子发票入口开放标识 非必填
     * Y，传入Y时，支付成功消息和支付详情页将出现开票入口。需要在微信支付商户平台或微信公众平台开通电子发票功能，传此字段才可生效
     */
    private String receipt;



    /**
     * 场景信息(scene_info)-门店id 非必填
     * 门店编号，由商户自定义
     */
    private String id;

    /**
     * 场景信息(scene_info)-门店名称 非必填
     * 门店名称 ，由商户自定义
     */
    private String name;

    /**
     * 场景信息(scene_info)-门店行政区划码 非必填
     * 门店所在地行政区划码
     */
    private String areaCode;

    /**
     * 场景信息(scene_info)-门店详细地址 非必填
     * 门店详细地址 ，由商户自定义
     */
    private String address;

}
