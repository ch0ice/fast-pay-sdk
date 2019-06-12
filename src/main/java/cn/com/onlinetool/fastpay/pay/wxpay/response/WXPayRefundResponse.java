package cn.com.onlinetool.fastpay.pay.wxpay.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author choice
 * @description: 微信 退款 响应对象
 * @date 2019-06-10 19:56
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WXPayRefundResponse extends WXPayBaseResponse {
    /**
     * 微信订单号 必填
     * 微信订单号
     */
    private String transactionId;

    /**
     * 商户订单号 必填
     * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
     */
    private String outTradeNo;

    /**
     * 商户退款单号 必填
     * 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
     */
    private String outRefundNo;

    /**
     * 微信退款单号 必填
     * 微信退款单号
     */
    private String refundId;

    /**
     * 退款金额 必填
     * 退款总金额,单位为分,可以做部分退款
     */
    private Integer refundFee;

    /**
     * 应结退款金额 非必填
     * 去掉非充值代金券退款金额后的退款金额，退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额
     */
    private Integer settlementRefundFee;

    /**
     * 标价金额 必填
     * 订单总金额，单位为分，只能为整数
     */
    private Integer totalFee;

    /**
     * 应结订单金额 非必填
     * 去掉非充值代金券金额后的订单总金额，应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
     */
    private Integer settlementTotalFee;

    /**
     * 标价币种 非必填
     * 订单金额货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY
     */
    private String feeType;

    /**
     * 现金支付金额 必填
     * 现金支付金额，单位为分，只能为整数
     */
    private Integer cashFee;

    /**
     * 现金支付币种 非必填
     * 货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY
     */
    private String cashFeeType;

    /**
     * 现金退款金额 非必填
     * 现金退款金额，单位为分，只能为整数
     */
    private Integer cashRefundFee;

    /**
     * 代金券信息 非必填
     * 自定义参数 存放以下微信返回的代金劵信息:
     * 代金券类型    coupon_type_$n      非必填
     *      CASH--充值代金券
     *      NO_CASH---非充值代金券
     *      订单使用代金券时有返回（取值：CASH、NO_CASH）。$n为下标,从0开始编号，举例：coupon_type_0
     * 代金券退款总金额     coupon_refund_fee    非必填    代金券退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金
     * 单个代金券退款金额    coupon_refund_fee_$n    非必填     代金券退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金
     * 退款代金券使用数量    coupon_refund_count    非必填     退款代金券使用数量
     * 退款代金券ID    coupon_refund_id_$n   非必填     退款代金券ID, $n为下标，从0开始编号
     */
    private Map<String,String> couponInfo;
}
