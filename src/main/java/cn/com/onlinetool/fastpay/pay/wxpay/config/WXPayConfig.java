package cn.com.onlinetool.fastpay.pay.wxpay.config;

import cn.com.onlinetool.fastpay.pay.wxpay.sdk.WXPayDomain;
import cn.com.onlinetool.fastpay.pay.wxpay.sdk.WXPayDomainSimpleImpl;
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
     * API 密钥
     */
    private String key;

    /**
     * 加密方式 默认 MD5
     */
    private String signType;

    /**
     * 支付回调url
     */
    private String notifyUrl;

    /**
     * 商户证书
     */
    private InputStream certStream;



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
