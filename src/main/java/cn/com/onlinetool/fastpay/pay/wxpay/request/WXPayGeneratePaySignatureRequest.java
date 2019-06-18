package cn.com.onlinetool.fastpay.pay.wxpay.request;

import cn.com.onlinetool.fastpay.annotations.validation.NotEmpty;
import cn.com.onlinetool.fastpay.annotations.validation.Validation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author choice
 * @description: 微信 生成支付签名 请求对象
 * @date 2019-06-12 10:05
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Validation
public class WXPayGeneratePaySignatureRequest{
    /**
     * 随机字符串 必填
     * 不要重新生成，使用统一支付返回
     */
    @NotEmpty
    private String nonceStr;
    /**
     * 支付id 必填
     * 统一支付返回的支付id
     */
    @NotEmpty
    private String prepayId;
}
