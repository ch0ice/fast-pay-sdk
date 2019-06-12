package cn.com.onlinetool.fastpay.pay.wxpay.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author choice
 * @description: 微信 退款查询 响应对象
 * @date 2019-06-10 20:31
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WXPayRefundQueryResponse extends WXPayBaseResponse {
    /**
     * 订单总退款次数 非必填
     * 订单总共已发生的部分退款次数，当请求参数传入offset后有返回
     */
    private String totalRefundCount;

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
     * 订单金额 必填
     * 订单总金额，单位为分，只能为整数
     */
    private Integer totalFee;

    /**
     * 应结订单金额 非必填
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
    private Integer cashFee;

    /**
     * 退款笔数 必填
     * 当前返回退款笔数
     */
    private Integer refundCount;

    /**
     * 退款信息 非必填
     * 自定义参数 存放以下微信返回的退款信息:
     * 商户退款单号   out_refund_no_$n    必填  商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
     * 微信退款单号   refund_id_$n    必填  微信退款单号
     * 退款渠道     refund_channel_$n   非必填
     *      ORIGINAL—原路退款
     *      BALANCE—退回到余额
     *      OTHER_BALANCE—原账户异常退到其他余额账户
     *      OTHER_BANKCARD—原银行卡异常退到其他银行卡
     * 申请退款金额   refund_fee_$n   必填      退款总金额,单位为分,可以做部分退款
     * 退款金额     settlement_refund_fee_$n    非必填     退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额
     * 退款状态     refund_status_$n    必填
     *      SUCCESS—退款成功
     *      REFUNDCLOSE—退款关闭。
     *      PROCESSING—退款处理中
     *      CHANGE—退款异常，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，可前往商户平台（pay.weixin.qq.com）-交易中心，手动处理此笔退款。$n为下标，从0开始编号。
     * 退款资金来源   refund_account_$n   非必填
     *      REFUND_SOURCE_RECHARGE_FUNDS---可用余额退款/基本账户
     *      REFUND_SOURCE_UNSETTLED_FUNDS---未结算资金退款
     *      $n为下标，从0开始编号。
     * 退款入账账户   refund_recv_accout_$n   必填
     *      取当前退款单的退款入账方
     *      1）退回银行卡：{银行名称}{卡类型}{卡尾号}
     *      2）退回支付用户零钱：支付用户零钱
     *      3）退还商户：商户基本账户/商户结算银行账户
     *      4）退回支付用户零钱通：支付用户零钱通
     * 退款成功时间   refund_success_time_$n      非必填     退款成功时间，当退款状态为退款成功时有返回。$n为下标，从0开始编号。
     */
    private Map<String,String> refundInfo;

    /**
     * 代金券信息 非必填
     * 自定义参数 存放以下微信返回的代金劵信息:
     * 代金券类型    coupon_type_$n_$m   非必填
     *      CASH--充值代金券
     *      NO_CASH---非充值优惠券
     *      开通免充值券功能，并且订单使用了优惠券后有返回（取值：CASH、NO_CASH）。$n为下标,$m为下标,从0开始编号，举例：coupon_type_$0_$1
     * 总代金券退款金额     coupon_refund_fee_$n    非必填     代金券退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金
     * 退款代金券使用数量    coupon_refund_count_$n      非必填     退款代金券使用数量 ,$n为下标,从0开始编号
     * 退款代金券ID      coupon_refund_id_$n_$m      非必填     退款代金券ID, $n为下标，$m为下标，从0开始编号
     * 单个代金券退款金额    coupon_refund_fee_$n_$m     非必填     单个退款代金券支付金额, $n为下标，$m为下标，从0开始编号
     */
    private Map<String,String> couponInfo;


}
