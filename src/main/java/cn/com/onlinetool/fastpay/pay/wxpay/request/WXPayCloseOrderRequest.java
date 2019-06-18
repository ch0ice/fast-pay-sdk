package cn.com.onlinetool.fastpay.pay.wxpay.request;

import cn.com.onlinetool.fastpay.annotations.validation.NotEmpty;
import cn.com.onlinetool.fastpay.annotations.validation.Validation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author choice
 * @description: 微信 关闭订单 请求对象
 * 应用场景：
 *      商户订单支付失败需要生成新单号重新发起支付，要对原订单号调用关单，避免重复支付；系统下单后，用户支付超时，系统退出不再受理，避免用户继续，请调用关单接口。
 * 注意：
 *      订单生成后不能马上调用关单接口，最短调用时间间隔为5分钟。
 * 是否需要证书：
 *      否
 * @date 2019-06-10 19:28
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Validation
public class WXPayCloseOrderRequest extends WXPayBaseRequest{
    /**
     * 商户订单号 必填
     * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
     */
    @NotEmpty
    private String outTradeNo;
}
