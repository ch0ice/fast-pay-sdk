package cn.com.onlinetool.fastpay.pay.wxpay.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author choice
 * @description: 微信 统一下单 响应对象
 * @date 2019-06-05 16:21
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WXPayUnifiedOrderResponse extends WXPayBaseResponse {

    /**
     * 交易类型 必填
     * 交易类型，取值为：JSAPI，NATIVE，APP等
     */
    private String tradeType;

    /**
     * 预支付交易会话标识 必填
     * 微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时
     */
    private String prepayId;

    /**
     * 二维码链接 非必填
     * trade_type=NATIVE时有返回，此url用于生成支付二维码，然后提供给用户进行扫码支付。
     */
    private String codeUrl;
}
