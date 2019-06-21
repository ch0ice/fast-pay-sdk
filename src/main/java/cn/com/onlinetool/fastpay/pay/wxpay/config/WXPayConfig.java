package cn.com.onlinetool.fastpay.pay.wxpay.config;

import cn.com.onlinetool.fastpay.constants.EncryptionTypeConstants;
import cn.com.onlinetool.fastpay.pay.wxpay.constants.WXPayConstants;
import cn.com.onlinetool.fastpay.pay.wxpay.domain.WXPayDomain;
import cn.com.onlinetool.fastpay.pay.wxpay.domain.WXPayDomainSimpleImpl;
import cn.com.onlinetool.fastpay.pay.wxpay.constants.WXPayTypeConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

/**
 * 微信支付配置
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WXPayConfig {

    /**
     * 小程序ID 必填
     * 微信分配的小程序ID
     */
    private String appid;

    /**
     * 商户号 必填
     * 微信支付分配的商户号
     */
    private String mchId;

    /**
     * 支付方式
     * /**
     * JSAPI--JSAPI支付（或小程序支付）、
     * NATIVE--Native支付、
     * APP--app支付，
     * MWEB--H5支付，
     * 不同trade_type决定了调起支付的方式，请根据支付产品正确上传
     */
    private String tradeType;

    /**
     * API 密钥
     */
    private String key;

    /**
     * 加密方式 默认 MD5
     */
    private String signType = EncryptionTypeConstants.MD5;

    /**
     * 商户证书
     */
    private InputStream cert;


    /**
     * 获取WXPayDomain, 用于多域名容灾自动切换
     */
    private WXPayDomain wxPayDomain = WXPayDomainSimpleImpl.instance();

    /**
     * HTTP(S) 连接超时时间，单位毫秒
     */
    private int httpConnectTimeoutMs = 6*1000;

    /**
     * HTTP(S) 读数据超时时间，单位毫秒
     */
    private int httpReadTimeoutMs = 8*1000;

    /**
     * 重试次数
     */
    private int retryNum = 0;


    /**
     * 是否自动上报。
     */
    private boolean autoReport = false;

    /**
     * 使用沙箱
     */
    private boolean useSandbox = false;

    /**
     * 进行健康上报的线程的数量
     */
    private int reportWorkerNum =  6;

    /**
     * 健康上报缓存消息的最大数量。会有线程去独立上报
     * 粗略计算：加入一条消息200B，10000消息占用空间 2000 KB，约为2MB，可以接受
     */
    private int reportQueueMaxSize = 10000;

    /**
     * 批量上报，一次最多上报多个数据
     */
    private int reportBatchSize = 10;

}
