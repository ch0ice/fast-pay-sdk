package cn.com.onlinetool.fastpay.pay.wxpay;

import cn.com.onlinetool.fastpay.pay.wxpay.sdk.WXPayDomain;
import cn.com.onlinetool.fastpay.pay.wxpay.config.WXPayConfig;

/**
 * 域名管理，实现主备域名自动切换
 */
public class MyWXPayDomain implements WXPayDomain {

    @Override
    public void report(String domain, long elapsedTimeMillis, Exception ex) {

    }

    @Override
    public WXPayDomain.DomainInfo getDomain(WXPayConfig config) {
        DomainInfo domainInfo = new DomainInfo("api.mch.weixin.qq.com",false);
        return domainInfo;
    }
}
