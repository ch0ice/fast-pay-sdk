package cn.com.onlinetool.fastpay.pay.wxpay.request;

import cn.com.onlinetool.fastpay.annotations.validation.NotEmpty;
import cn.com.onlinetool.fastpay.annotations.validation.Validation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author choice
 * @description: 微信 下载资金账单 请求对象
 * 应用场景：
 *      商户可以通过该接口下载自2017年6月1日起 的历史资金流水账单。
 *      说明：
 *      1、资金账单中的数据反映的是商户微信账户资金变动情况；
 *      2、当日账单在次日上午9点开始生成，建议商户在上午10点以后获取；
 *      3、资金账单中涉及金额的字段单位为“元”。
 * 是否需要证书：
 *      请求需要双向证书。
 * 注意：
 *      此接口目前签名类型只支持 HMAC-SHA256
 * @date 2019-06-11 10:38
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Validation
public class WXPayDownloadFundFlowRequest extends WXPayBaseRequest {
    /**
     * 对账单日期    必填
     * 下载对账单的日期，格式：20140603
     */
    @NotEmpty
    private String billDate;

    /**
     * 资金账户类型     必填
     * 账单的资金来源账户：
     * Basic  基本账户
     * Operation 运营账户
     * Fees 手续费账户
     */
    @NotEmpty
    private String accountType;

    /**
     * 压缩账单     非必填
     * 非必传参数，固定值：GZIP，返回格式为.gzip的压缩包账单。不传则默认为数据流形式。
     */
    private String tarType;
}
