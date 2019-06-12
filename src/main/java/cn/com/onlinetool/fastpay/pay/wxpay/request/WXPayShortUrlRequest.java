package cn.com.onlinetool.fastpay.pay.wxpay.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author choice
 * @description: 微信 转换短链接 请求对象
 * 应用场景：
 *      该接口主要用于Native支付模式一中的二维码链接转成短链接(weixin://wxpay/s/XXXXXX)，减小二维码数据量，提升扫描速度和精确度。
 * 是否需要证书：
 *      不需要
 * @date 2019-06-11 14:39
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WXPayShortUrlRequest extends WXPayBaseRequest{
    /**
     * URL链接 必传
     * 需要转换的URL，签名用原串，传输需URLencode
     */
    private String longUrl;
}
