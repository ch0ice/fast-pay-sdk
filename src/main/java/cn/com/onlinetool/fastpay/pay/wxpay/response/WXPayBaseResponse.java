package cn.com.onlinetool.fastpay.pay.wxpay.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author choice
 * @description: 微信支付基础请求字段
 * @date 2019-06-10 10:57
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WXPayBaseResponse {
    /**
     * 返回状态码 必填
     * SUCCESS/FAIL 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
     */
    private String returnCode;

    /**
     * 返回信息 非必填
     * 返回信息，如非空，为错误原因
     */
    private String returnMsg;



    /**
     * return_code == SUCCESS 时返回
     * 小程序ID 必填
     * 微信分配的小程序ID
     */
    private String appid;

    /**
     * return_code == SUCCESS 时返回
     * 商户号 必填
     * 微信支付分配的商户号
     */
    private String mchId;

    /**
     * return_code == SUCCESS 时返回
     * 随机字符串 必填
     * 随机字符串，长度要求在32位以内
     */
    private String nonceStr;

    /**
     * return_code == SUCCESS 时返回
     * 签名 必填
     * 通过签名算法计算得出的签名值
     */
    private String sign;

    /**
     * 设备号 非必填
     * 自定义参数，可以为请求支付的终端设备号等
     */
    private String deviceInfo;

    /**
     * return_code == SUCCESS 时返回
     * 业务结果 必填
     * SUCCESS/FAIL
     */
    private String resultCode;

    /**
     * return_code == SUCCESS 时返回
     * 业务结果描述 必填
     * 对于业务执行的详细描述
     */
    private String resultMsg;

    /**
     * return_code == SUCCESS 时返回
     * 错误代码 非必填
     */
    private String errCode;

    /**
     * return_code == SUCCESS 时返回
     * 错误代码描述 非必填
     */
    private String errCodeDes;
}
