package cn.com.onlinetool.fastpay.pay.wxpay.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author choice
 * @description: 微信 生成支付签名 响应对象
 * @date 2019-06-12 10:45
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WXPayGeneratePaySignatureResponse {
    private String appId;
    private String timeStamp;
    private String nonceStr;
    private String prepayId;
    private String signType;
    private String sign;
}
