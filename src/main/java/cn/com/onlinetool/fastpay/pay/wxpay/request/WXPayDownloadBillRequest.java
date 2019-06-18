package cn.com.onlinetool.fastpay.pay.wxpay.request;

import cn.com.onlinetool.fastpay.annotations.validation.NotEmpty;
import cn.com.onlinetool.fastpay.annotations.validation.Validation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author choice
 * @description: 微信 下载对账单 请求对象
 * 应用场景:
 *      商户可以通过该接口下载历史交易清单。比如掉单、系统错误等导致商户侧和微信侧数据不一致，通过对账单核对后可校正支付状态。
 * 注意：
 *      1、微信侧未成功下单的交易不会出现在对账单中。支付成功后撤销的交易会出现在对账单中，跟原支付单订单号一致；
 *      2、微信在次日9点启动生成前一天的对账单，建议商户10点后再获取；
 *      3、对账单中涉及金额的字段单位为“元”。
 *      4、对账单接口只能下载三个月以内的账单。
 *      5、对账单是以商户号纬度来生成的，如一个商户号与多个appid有绑定关系，则使用其中任何一个appid都可以请求下载对账单。对账单中的appid取自交易时候提交的appid，与请求下载对账单时使用的appid无关。
 * 是否需要证书：
 *      不需要。
 * @date 2019-06-11 10:30
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Validation
public class WXPayDownloadBillRequest extends WXPayBaseRequest{
    /**
     * 对账单日期    必填
     * 下载对账单的日期，格式：20140603
     */
    @NotEmpty
    private String billDate;

    /**
     * 账单类型     非必填
     * ALL（默认值），返回当日所有订单信息（不含充值退款订单）
     * SUCCESS，返回当日成功支付的订单（不含充值退款订单）
     * REFUND，返回当日退款订单（不含充值退款订单）
     * RECHARGE_REFUND，返回当日充值退款订单
     */
    private String billType;

    /**
     * 压缩账单     非必填
     * 非必传参数，固定值：GZIP，返回格式为.gzip的压缩包账单。不传则默认为数据流形式。
     */
    private String tarType;
}
