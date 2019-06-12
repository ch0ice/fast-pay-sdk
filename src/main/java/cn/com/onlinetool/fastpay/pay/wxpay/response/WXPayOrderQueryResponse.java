package cn.com.onlinetool.fastpay.pay.wxpay.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author choice
 * @description: 微信 订单查询 响应
 * @date 2019-06-10 10:17
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WXPayOrderQueryResponse extends WXPayBaseResponse{
    /**
     * 用户标识 必填
     * 用户在商户appid下的唯一标识
     */
    private String openid;

    /**
     * 是否关注公众账号 必填
     * 用户是否关注公众账号，Y-关注，N-未关注
     */
    private String isSubscribe;

    /**
     * 交易类型 必填
     * 调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP，MICROPAY
     */
    private String tradeType;

    /**
     * 交易状态 必填
     * SUCCESS—支付成功
     * REFUND—转入退款
     * NOTPAY—未支付
     * CLOSED—已关闭
     * REVOKED—已撤销（刷卡支付）
     * USERPAYING--用户支付中
     * PAYERROR--支付失败(其他原因，如银行返回失败)
     */
    private String tradeState;

    /**
     * 付款银行 必填
     * 银行类型，采用字符串类型的银行标识
     */
    private String bankType;

    /**
     * 标价金额 必填
     * 订单总金额，单位为分
     */
    private Integer totalFee;

    /**
     * 应结订单金额 必填
     * 当订单使用了免充值型优惠券后返回该参数，应结订单金额=订单金额-免充值优惠券金额。
     */
    private Integer settlementTotalFee;

    /**
     * 标价币种 非必填
     * 货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY
     */
    private String feeType;

    /**
     * 现金支付金额 必填
     * 现金支付金额订单现金支付金额
     */
    private String cashFee;

    /**
     * 现金支付币种 非必填
     * 货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY
     */
    private String cashFeeType;

    /**
     * 微信支付订单号 必填
     * 微信支付订单号
     */
    private String transactionId;

    /**
     * 商户订单号 必填
     * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
     */
    private String outTradeNo;

    /**
     * 附加数据 非必填
     * 附加数据，原样返回
     */
    private String attach;

    /**
     * 支付完成时间 必填
     * 订单支付时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。
     */
    private String timeEnd;

    /**
     * 交易状态描述 必填
     * 对当前查询订单状态的描述和下一步操作的指引
     */
    private String tradeStateDesc;

    /**
     * 代金券信息 非必填
     * 自定义参数 存放以下微信返回的代金劵信息:
     * 代金券金额    coupon_fee      非必填      “代金券”金额<=订单金额，订单金额-“代金券”金额=现金支付金额
     * 代金券使用数量  coupon_count    非必填    代金券使用数量
     * 代金券类型    coupon_type_$n    非必填     （n ==）
     *      CASH--充值代金券
     *      NO_CASH---非充值优惠券
     *      开通免充值券功能，并且订单使用了优惠券后有返回（取值：CASH、NO_CASH）。$n为下标,从0开始编号，举例：coupon_type_$0
     * 代金券ID    coupon_id_$n    非必填     代金券ID, $n为下标，从0开始编号
     * 单个代金券支付金额    coupon_fee_$n   非必填     单个代金券支付金额, $n为下标，从0开始编号
     */
    private Map<String,String> couponInfo;


}
