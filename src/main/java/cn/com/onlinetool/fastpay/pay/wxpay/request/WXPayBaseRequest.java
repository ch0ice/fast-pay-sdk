package cn.com.onlinetool.fastpay.pay.wxpay.request;

import cn.com.onlinetool.fastpay.annotations.validation.NotEmpty;
import cn.com.onlinetool.fastpay.annotations.validation.Validation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author choice
 * @description: 微信支付基础请求字段
 * @date 2019-06-10 18:21
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Validation
public class WXPayBaseRequest {
    /**
     * 小程序ID 必填
     * 微信分配的小程序ID
     */
    @NotEmpty
    private String appid;

    /**
     * 商户号 必填
     * 微信支付分配的商户号
     */
    @NotEmpty
    private String mchId;

    /**
     * 随机字符串 必填
     * 随机字符串，长度要求在32位以内
     */
    @NotEmpty
    private String nonceStr;

    /**
     * 签名 必填
     * 通过签名算法计算得出的签名值
     */
    @NotEmpty
    private String sign;

    /**
     * 签名类型 非必填
     * 签名类型，默认为MD5，支持HMAC-SHA256和MD5。
     */
    private String signType;

    /**
     * 设备号 非必填
     * 自定义参数，可以为请求支付的终端设备号等
     */
    private String deviceInfo;
}
