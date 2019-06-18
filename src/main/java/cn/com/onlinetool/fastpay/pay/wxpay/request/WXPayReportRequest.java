package cn.com.onlinetool.fastpay.pay.wxpay.request;

import cn.com.onlinetool.fastpay.annotations.validation.NotEmpty;
import cn.com.onlinetool.fastpay.annotations.validation.Validation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author choice
 * @description: 微信 交易保障 请求对象
 * 应用场景：
 *      商户在调用微信支付提供的相关接口时，会得到微信支付返回的相关信息以及获得整个接口的响应时间。
 *      为提高整体的服务水平，协助商户一起提高服务质量，微信支付提供了相关接口调用耗时和返回信息的主动上报接口，
 *      微信支付可以根据商户侧上报的数据进一步优化网络部署，完善服务监控，和商户更好的协作为用户提供更好的业务体验。
 * 是否需要证书：
 *      不需要
 * @date 2019-06-11 10:46
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Validation
public class WXPayReportRequest extends WXPayBaseRequest {
    /**
     * 接口URL    必填
     * 报对应的接口的完整URL，类似：https://api.mch.weixin.qq.com/pay/unifiedorder
     * 对于刷卡支付，为更好的和商户共同分析一次业务行为的整体耗时情况，对于两种接入模式，请都在门店侧对一次刷卡支付进行一次单独的整体上报，
     * 上报URL指定为： https://api.mch.weixin.qq.com/pay/micropay/total
     * 其它接口调用仍然按照调用一次，上报一次来进行。
     */
    @NotEmpty
    private String interfaceUrl;

    /**
     * 接口耗时     必填
     * 接口耗时情况，单位为毫秒
     */
    @NotEmpty
    private Integer executeTime;

    /**
     * 返回状态码	必填
     * SUCCESS/FAIL
     * 此字段是通信标识，非交易标识，交易是否成功需要查看trade_state来判断
     */
    @NotEmpty
    private String returnCode;

    /**
     * 返回信息     非必填
     * 返回信息，如非空，为错误原因
     * 签名失败
     * 参数格式校验错误
     */
    private String returnMsg;

    /**
     * 业务结果 必填
     * SUCCESS/FAIL
     */
    @NotEmpty
    private String resultCode;

    /**
     * 错误代码 非必填
     */
    private String errCode;

    /**
     * 错误代码描述 非必填
     */
    private String errCodeDes;

    /**
     * 商户订单号 非必填
     * 商户系统内部的订单号,商户可以在上报时提供相关商户订单号方便微信支付更好的提高服务质量。
     */
    private String outTradeNo;

    /**
     * 访问接口IP   必填
     * 发起接口调用时的机器IP
     */
    @NotEmpty
    private String userIp;

    /**
     * 商户上报时间   非必填     系统时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。
     */
    private String time;
}
