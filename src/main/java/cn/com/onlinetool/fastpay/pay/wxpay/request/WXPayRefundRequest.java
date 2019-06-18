package cn.com.onlinetool.fastpay.pay.wxpay.request;

import cn.com.onlinetool.fastpay.annotations.validation.NotEmpty;
import cn.com.onlinetool.fastpay.annotations.validation.Validation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author choice
 * @description: 微信 退款 请求对象
 * 应用场景：
 *      当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，
 *      微信支付将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。
 * 注意：
 *      1、交易时间超过一年的订单无法提交退款
 *      2、微信支付退款支持单笔交易分多次退款，多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。申请退款总金额不能超过订单金额。
 *          一笔退款失败后重新提交，请不要更换退款单号，请使用原商户退款单号
 *      3、请求频率限制：150qps，即每秒钟正常的申请退款请求次数不超过150次
 *          错误或无效请求频率限制：6qps，即每秒钟异常或错误的退款申请请求不超过6次
 *      4、每个支付订单的部分退款次数不能超过50次
 * 是否需要证书：
 *      请求需要双向证书。 详见证书使用
 * @date 2019-06-10 19:54
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Validation
public class WXPayRefundRequest extends WXPayBaseRequest{
    /**
     * 微信订单号 微信订单号/商户订单号 二选一
     * 微信生成的订单号，在支付通知中有返回
     */
    private String transactionId;

    /**
     * 商户订单号 微信订单号/商户订单号 二选一
     * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
     */
    private String outTradeNo;

    /**
     * 商户退款单号 必填
     * 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
     */
    @NotEmpty
    private String outRefundNo;

    /**
     * 订单金额 必填
     * 订单总金额，单位为分，只能为整数
     */
    @NotEmpty
    private Integer totalFee;

    /**
     * 退款金额 必填
     * 退款总金额，订单总金额，单位为分，只能为整数
     */
    @NotEmpty
    private Integer refundFee;

    /**
     * 货币种类 非必填
     * 货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY
     */
    private String refundFeeType;

    /**
     * 退款原因 非必填
     * 若商户传入，会在下发给用户的退款消息中体现退款原因
     * 注意：若订单退款金额≤1元，且属于部分退款，则不会在退款消息中体现退款原因
     */
    private String refundDesc;

    /**
     * 退款资金来源 非必填
     * 仅针对老资金流商户使用
     * REFUND_SOURCE_UNSETTLED_FUNDS---未结算资金退款（默认使用未结算资金退款）
     * REFUND_SOURCE_RECHARGE_FUNDS---可用余额退款
     */
    private String refundAccount;

    /**
     * 退款结果通知url 非必填
     * 异步接收微信支付退款结果通知的回调地址，通知URL必须为外网可访问的url，不允许带参数
     * 如果参数中传了notify_url，则商户平台上配置的回调地址将不会生效。
     */
    private String notifyUrl;
}
