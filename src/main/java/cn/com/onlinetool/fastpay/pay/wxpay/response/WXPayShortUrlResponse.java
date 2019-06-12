package cn.com.onlinetool.fastpay.pay.wxpay.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author choice
 * @description: 微信 转换短链接 响应对象
 * @date 2019-06-11 14:42
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WXPayShortUrlResponse extends WXPayBaseResponse {
    /**
     * URL链接    必填
     * 转换后的URL
     */
    private String shortUrl;
}
