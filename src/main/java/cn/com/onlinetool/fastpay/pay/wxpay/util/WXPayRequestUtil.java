package cn.com.onlinetool.fastpay.pay.wxpay.util;

import cn.com.onlinetool.fastpay.constants.EncryptionTypeConstants;
import cn.com.onlinetool.fastpay.pay.wxpay.config.WXPayConfig;
import cn.com.onlinetool.fastpay.pay.wxpay.constants.WXPayConstants;
import cn.com.onlinetool.fastpay.pay.wxpay.domain.WXPayDomain;
import cn.com.onlinetool.fastpay.pay.wxpay.domain.WXPayReport;
import cn.com.onlinetool.fastpay.util.ConverterUtil;
import cn.com.onlinetool.fastpay.util.OkHttpRequestUtil;
import org.apache.http.conn.ConnectTimeoutException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

import static cn.com.onlinetool.fastpay.pay.wxpay.constants.WXPayConstants.USER_AGENT;

public class WXPayRequestUtil {
    private WXPayConfig config;
    public WXPayRequestUtil(WXPayConfig config){
        this.config = config;
    }


    private String request(String urlSuffix, String uuid, String data, int connectTimeoutMs, int readTimeoutMs, boolean useCert, boolean autoReport) throws Exception {
        Exception exception = null;
        long elapsedTimeMillis = 0;
        long startTimestampMs = WXPayUtil.getCurrentTimestampMs();
        boolean firstHasDnsErr = false;
        boolean firstHasConnectTimeout = false;
        boolean firstHasReadTimeout = false;
        WXPayDomain.DomainInfo domainInfo = config.getWxPayDomain().getDomain(config);
        if(domainInfo == null){
            throw new Exception("WXPayConfig.getWXPayDomain().getDomain() is empty or null");
        }
        try {
            String url = domainInfo.domain + urlSuffix;
            String contentType = "application/xml; charset=utf-8";
            String result;
            if(null != config.getCert() && config.getRetryNum() > 0){
                result = OkHttpRequestUtil.syncPostRequest(url,data,contentType,config.getRetryNum(),config.getCert(),"PKCS12",config.getMchId()).body().string();
            }else if(null != config.getCert()){
                result = OkHttpRequestUtil.syncPostRequest(url,data,contentType,config.getCert(),"PKCS12",config.getMchId()).body().string();
            }else if(config.getRetryNum() > 0){
                result = OkHttpRequestUtil.syncPostRequest(url,data,contentType,config.getRetryNum()).body().string();
            }else {
                result = OkHttpRequestUtil.syncPostRequest(url,data,contentType).body().string();
            }
            elapsedTimeMillis = WXPayUtil.getCurrentTimestampMs()-startTimestampMs;
            config.getWxPayDomain().report(domainInfo.domain, elapsedTimeMillis, null);
            WXPayReport.getInstance(config).report(
                    uuid,
                    elapsedTimeMillis,
                    domainInfo.domain,
                    domainInfo.primaryDomain,
                    connectTimeoutMs,
                    readTimeoutMs,
                    firstHasDnsErr,
                    firstHasConnectTimeout,
                    firstHasReadTimeout);
            return result;
        }
        catch (UnknownHostException ex) {  // dns 解析错误，或域名不存在
            exception = ex;
            firstHasDnsErr = true;
            elapsedTimeMillis = WXPayUtil.getCurrentTimestampMs()-startTimestampMs;
            WXPayUtil.getLogger().warn("UnknownHostException for domainInfo {}", domainInfo);
            WXPayReport.getInstance(config).report(
                    uuid,
                    elapsedTimeMillis,
                    domainInfo.domain,
                    domainInfo.primaryDomain,
                    connectTimeoutMs,
                    readTimeoutMs,
                    firstHasDnsErr,
                    firstHasConnectTimeout,
                    firstHasReadTimeout
            );
        }
        catch (ConnectTimeoutException ex) {
            exception = ex;
            firstHasConnectTimeout = true;
            elapsedTimeMillis = WXPayUtil.getCurrentTimestampMs()-startTimestampMs;
            WXPayUtil.getLogger().warn("connect timeout happened for domainInfo {}", domainInfo);
            WXPayReport.getInstance(config).report(
                    uuid,
                    elapsedTimeMillis,
                    domainInfo.domain,
                    domainInfo.primaryDomain,
                    connectTimeoutMs,
                    readTimeoutMs,
                    firstHasDnsErr,
                    firstHasConnectTimeout,
                    firstHasReadTimeout
            );
        }
        catch (SocketTimeoutException ex) {
            exception = ex;
            firstHasReadTimeout = true;
            elapsedTimeMillis = WXPayUtil.getCurrentTimestampMs()-startTimestampMs;
            WXPayUtil.getLogger().warn("timeout happened for domainInfo {}", domainInfo);
            WXPayReport.getInstance(config).report(
                    uuid,
                    elapsedTimeMillis,
                    domainInfo.domain,
                    domainInfo.primaryDomain,
                    connectTimeoutMs,
                    readTimeoutMs,
                    firstHasDnsErr,
                    firstHasConnectTimeout,
                    firstHasReadTimeout);
        }
        catch (Exception ex) {
            exception = ex;
            elapsedTimeMillis = WXPayUtil.getCurrentTimestampMs()-startTimestampMs;
            WXPayReport.getInstance(config).report(
                    uuid,
                    elapsedTimeMillis,
                    domainInfo.domain,
                    domainInfo.primaryDomain,
                    connectTimeoutMs,
                    readTimeoutMs,
                    firstHasDnsErr,
                    firstHasConnectTimeout,
                    firstHasReadTimeout);
        }
        config.getWxPayDomain().report(domainInfo.domain, elapsedTimeMillis, exception);
        throw exception;
    }


    /**
     * 可重试的，非双向认证的请求
     * @param uuid
     * @param data
     * @return
     */
    public String requestWithoutCert(String urlSuffix,String uuid, String data, boolean autoReport) throws Exception {
        return this.request(urlSuffix, uuid, data, config.getHttpConnectTimeoutMs(), config.getHttpReadTimeoutMs(), false, autoReport);
    }

    /**
     * 可重试的，非双向认证的请求
     * @param uuid
     * @param data
     * @param connectTimeoutMs
     * @param readTimeoutMs
     * @return
     */
    public String requestWithoutCert(String urlSuffix,String uuid, String data, int connectTimeoutMs, int readTimeoutMs,  boolean autoReport) throws Exception {
        return this.request(urlSuffix, uuid, data, connectTimeoutMs, readTimeoutMs, false, autoReport);
    }

    /**
     * 可重试的，双向认证的请求
     * @param urlSuffix
     * @param uuid
     * @param data
     * @return
     */
    public String requestWithCert(String urlSuffix, String uuid, String data, boolean autoReport) throws Exception {
        return this.request(urlSuffix, uuid, data, config.getHttpConnectTimeoutMs(), config.getHttpReadTimeoutMs(), true, autoReport);
    }

    /**
     * 可重试的，双向认证的请求
     * @param urlSuffix
     * @param uuid
     * @param data
     * @param connectTimeoutMs
     * @param readTimeoutMs
     * @return
     */
    public String requestWithCert(String urlSuffix, String uuid, String data, int connectTimeoutMs, int readTimeoutMs, boolean autoReport) throws Exception {
        return this.request(urlSuffix, uuid, data, connectTimeoutMs, readTimeoutMs, true, autoReport);
    }

    /**
     * 不需要证书的请求
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     * @throws Exception
     */
    public String requestWithoutCert(Map<String, String> reqData) throws Exception {
        String urlSuffix;
        if (config.isUseSandbox()) {
            urlSuffix = WXPayConstants.SANDBOX_ORDERQUERY_URL_SUFFIX;
        }
        else {
            urlSuffix = WXPayConstants.ORDERQUERY_URL_SUFFIX;
        }
        String msgUUID = reqData.get("nonce_str");
        String reqBody = ConverterUtil.mapToXml(reqData);

        String resp = this.requestWithoutCert(urlSuffix, msgUUID, reqBody, config.getHttpConnectTimeoutMs(), config.getHttpReadTimeoutMs(), config.isAutoReport());
        return resp;
    }


    /**
     * 需要证书的请求
     * @param reqData 向wxpay post的请求数据  Map
     * @return API返回数据
     * @throws Exception
     */
    public String requestWithCert(Map<String, String> reqData) throws Exception {
        String urlSuffix;
        if (config.isUseSandbox()) {
            urlSuffix = WXPayConstants.SANDBOX_ORDERQUERY_URL_SUFFIX;
        }
        else {
            urlSuffix = WXPayConstants.ORDERQUERY_URL_SUFFIX;
        }
        String msgUUID= reqData.get("nonce_str");
        String reqBody = ConverterUtil.mapToXml(reqData);

        String resp = this.requestWithCert(urlSuffix, msgUUID, reqBody, config.getHttpConnectTimeoutMs(), config.getHttpReadTimeoutMs(), config.isAutoReport());
        return resp;
    }


    /**
     * 向 Map 中添加 appid、mch_id、nonce_str、sign_type、sign <br>
     * 该函数适用于商户适用于统一下单等接口，不适用于红包、代金券接口
     *
     * @param reqData
     * @return
     * @throws Exception
     */
    public Map<String, String> fillRequestData(Map<String, String> reqData) throws Exception {
        reqData.put("appid", config.getAppid());
        reqData.put("mch_id", config.getMchId());
        reqData.put("nonce_str", WXPayUtil.generateNonceStr());
        if (EncryptionTypeConstants.MD5.equals(config.getSignType())) {
            reqData.put("sign_type", WXPayConstants.MD5);
        }
        else if (WXPayConstants.SignType.HMACSHA256.equals(config.getSignType())) {
            reqData.put("sign_type", WXPayConstants.HMACSHA256);
        }
        reqData.put("sign", WXPayUtil.generateSignature(reqData, config.getKey(), config.getSignType()));
        return reqData;
    }


}
