package cn.com.onlinetool.fastpay.pay.wxpay.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author choice
 * @description: 微信 退款查询 请求对象
 * 应用场景：
 *      提交退款申请后，通过调用该接口查询退款状态。退款有一定延时，用零钱支付的退款20分钟内到账，银行卡支付的退款3个工作日后重新查询退款状态。
 * 注意：
 *      如果单个支付订单部分退款次数超过20次请使用退款单号查询
 * 分页查询：
 *      当一个订单部分退款超过10笔后，商户用微信订单号或商户订单号调退款查询API查询退款时，默认返回前10笔和total_refund_count（订单总退款次数）。
 *      商户需要查询同一订单下超过10笔的退款单时，可传入订单号及offset来查询，微信支付会返回offset及后面的10笔，以此类推。
 *      当商户传入的offset超过total_refund_count，则系统会返回报错PARAM_ERROR。
 * 举例：
 *      一笔订单下的退款单有36笔，当商户想查询第25笔时，可传入订单号及offset=24，微信支付平台会返回第25笔到第35笔的退款单信息，或商户可直接传入退款单号查询退款
 * 是否需要证书：
 *      不需要。
 * @date 2019-06-10 20:30
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WXPayRefundQueryRequest extends WXPayBaseRequest {
    /**
     * 微信订单号 微信订单号/商户订单号/商户退款单号/微信退款单号 四选一
     * 微信生成的订单号，在支付通知中有返回
     */
    private String transactionId;

    /**
     * 商户订单号 微信订单号/商户订单号/商户退款单号/微信退款单号 四选一
     * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
     */
    private String outTradeNo;

    /**
     * 商户退款单号 微信订单号/商户订单号/商户退款单号/微信退款单号 四选一
     * 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
     */
    private String outRefundNo;

    /**
     * 微信退款单号 微信订单号/商户订单号/商户退款单号/微信退款单号 四选一
     * 微信生成的退款单号，在申请退款接口有返回
     */
    private String refundId;

    /**
     * 偏移量 非必填
     * 偏移量，当部分退款次数超过10次时可使用，表示返回的查询结果从这个偏移量开始取记录
     */
    private Integer offset;
}
