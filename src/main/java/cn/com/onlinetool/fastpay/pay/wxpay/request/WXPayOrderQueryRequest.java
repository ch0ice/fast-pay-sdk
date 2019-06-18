package cn.com.onlinetool.fastpay.pay.wxpay.request;

import cn.com.onlinetool.fastpay.annotations.validation.Validation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author choice
 * @description: 微信 订单查询 请求对象
 * 应用场景：
 *      该接口提供所有微信支付订单的查询，商户可以通过查询订单接口主动查询订单状态，完成下一步的业务逻辑。
 * 需要调用查询接口的情况：
 *      ◆ 当商户后台、网络、服务器等出现异常，商户系统最终未接收到支付通知；
 *      ◆ 调用支付接口后，返回系统错误或未知交易状态情况；
 *      ◆ 调用刷卡支付API，返回USERPAYING的状态；
 *      ◆ 调用关单或撤销接口API之前，需确认支付状态；
 * 是否需要证书：
 *      否
 * @date 2019-06-10 10:17
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Validation
public class WXPayOrderQueryRequest extends WXPayBaseRequest{
    /**
     * 微信订单号 微信订单号/商户订单号 二选一
     * 微信的订单号，优先使用
     */
    private String transactionId;

    /**
     * 商户订单号 微信订单号/商户订单号 二选一
     * 商户自定义订单号
     */
    private String outTradeNo;

}
