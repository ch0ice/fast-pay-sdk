package cn.com.onlinetool.fastpay.pay.wxpay.util;

import cn.com.onlinetool.fastpay.constants.EncryptionTypeConstants;
import cn.com.onlinetool.fastpay.pay.wxpay.config.WXPayConfig;
import cn.com.onlinetool.fastpay.pay.wxpay.constants.WXPayConstants;
import cn.com.onlinetool.fastpay.pay.wxpay.domain.WXPayDomain;
import cn.com.onlinetool.fastpay.pay.wxpay.domain.WXPayReport;
import cn.com.onlinetool.fastpay.util.ConverterUtil;
import cn.com.onlinetool.fastpay.util.OkHttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Map;

import static cn.com.onlinetool.fastpay.pay.wxpay.constants.WXPayConstants.USER_AGENT;

@Slf4j
public class WXPayRequestUtil {
    private WXPayConfig config;

    public WXPayRequestUtil(WXPayConfig config) {
        this.config = config;
    }

    /**
     * 请求，只请求一次，不做重试
     * @param domain
     * @param urlSuffix
     * @param uuid
     * @param data
     * @param connectTimeoutMs
     * @param readTimeoutMs
     * @param useCert 是否使用证书，针对退款、撤销等操作
     * @return
     * @throws Exception
     */
    private String requestOnce(final String domain, String urlSuffix, String uuid, String data, int connectTimeoutMs, int readTimeoutMs, boolean useCert) throws Exception {
        BasicHttpClientConnectionManager connManager;
        if (useCert) {
            // 证书
            char[] password = config.getMchId().toCharArray();
            InputStream certStream = config.getCert();
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(certStream, password);

            // 实例化密钥库 & 初始化密钥工厂
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, password);

            // 创建 SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());

            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    sslContext,
                    new String[]{"TLSv1"},
                    null,
                    new DefaultHostnameVerifier());

            connManager = new BasicHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", sslConnectionSocketFactory)
                            .build(),
                    null,
                    null,
                    null
            );
        }
        else {
            connManager = new BasicHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", SSLConnectionSocketFactory.getSocketFactory())
                            .build(),
                    null,
                    null,
                    null
            );
        }

        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connManager)
                .build();

        String url = "https://" + domain + urlSuffix;
        HttpPost httpPost = new HttpPost(url);

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeoutMs).setConnectTimeout(connectTimeoutMs).build();
        httpPost.setConfig(requestConfig);

        StringEntity postEntity = new StringEntity(data, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.addHeader("User-Agent", USER_AGENT + " " + config.getMchId());
        httpPost.setEntity(postEntity);

        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        return EntityUtils.toString(httpEntity, "UTF-8");

    }

    private String request(String urlSuffix, String uuid, String data, int connectTimeoutMs, int readTimeoutMs, boolean useCert, boolean autoReport) throws Exception {
        Exception exception = null;
        long elapsedTimeMillis = 0;
        long startTimestampMs = WXPayUtil.getCurrentTimestampMs();
        boolean firstHasDnsErr = false;
        boolean firstHasConnectTimeout = false;
        boolean firstHasReadTimeout = false;
        WXPayDomain.DomainInfo domainInfo = config.getWxPayDomain().getDomain(config);
        if (domainInfo == null) {
            throw new Exception("WXPayConfig.getWXPayDomain().getDomain() is empty or null");
        }
        try {
//            String result = requestOnce(domainInfo.domain, urlSuffix, uuid, data, connectTimeoutMs, readTimeoutMs, useCert);

            String url = domainInfo.domain + urlSuffix;
            String contentType = "application/xml; charset=utf-8";
            String result;
            if (null != config.getCert() && config.getRetryNum() > 0) {
                result = OkHttpRequestUtil.syncPostRequest(url, data, contentType, USER_AGENT, config.getRetryNum(), config.getCert(), "PKCS12", config.getMchId()).body().string();
            } else if (null != config.getCert()) {
                result = OkHttpRequestUtil.syncPostRequest(url, data, contentType, USER_AGENT, config.getCert(), "PKCS12", config.getMchId()).body().string();
            } else if (config.getRetryNum() > 0) {
                result = OkHttpRequestUtil.syncPostRequest(url, data, contentType, USER_AGENT, config.getRetryNum()).body().string();
            } else {
                result = OkHttpRequestUtil.syncPostRequest(url, data, contentType, USER_AGENT).body().string();
            }

            elapsedTimeMillis = WXPayUtil.getCurrentTimestampMs() - startTimestampMs;
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
        } catch (UnknownHostException ex) {  // dns 解析错误，或域名不存在
            exception = ex;
            firstHasDnsErr = true;
            elapsedTimeMillis = WXPayUtil.getCurrentTimestampMs() - startTimestampMs;
            log.warn("UnknownHostException for domainInfo {}", domainInfo);
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
        } catch (SocketTimeoutException ex) {
            exception = ex;
            firstHasReadTimeout = true;
            elapsedTimeMillis = WXPayUtil.getCurrentTimestampMs() - startTimestampMs;
            log.warn("timeout happened for domainInfo {}", domainInfo);
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
        } catch (Exception ex) {
            exception = ex;
            elapsedTimeMillis = WXPayUtil.getCurrentTimestampMs() - startTimestampMs;
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
     *
     * @param uuid
     * @param data
     * @return
     */
    public String requestWithoutCert(String urlSuffix, String uuid, String data, boolean autoReport) throws Exception {
        return this.request(urlSuffix, uuid, data, config.getHttpConnectTimeoutMs(), config.getHttpReadTimeoutMs(), false, autoReport);
    }

    /**
     * 可重试的，非双向认证的请求
     *
     * @param uuid
     * @param data
     * @param connectTimeoutMs
     * @param readTimeoutMs
     * @return
     */
    public String requestWithoutCert(String urlSuffix, String uuid, String data, int connectTimeoutMs, int readTimeoutMs, boolean autoReport) throws Exception {
        return this.request(urlSuffix, uuid, data, connectTimeoutMs, readTimeoutMs, false, autoReport);
    }

    /**
     * 可重试的，双向认证的请求
     *
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
     *
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
     *
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     * @throws Exception
     */
    public String requestWithoutCert(Map<String, String> reqData) throws Exception {
        String urlSuffix;
        if (config.isUseSandbox()) {
            urlSuffix = WXPayConstants.SANDBOX_UNIFIEDORDER_URL_SUFFIX;
        } else {
            urlSuffix = WXPayConstants.UNIFIEDORDER_URL_SUFFIX;
        }
        String msgUUID = reqData.get("nonce_str");
        String reqBody = ConverterUtil.mapToXml(reqData);

        String resp = this.requestWithoutCert(urlSuffix, msgUUID, reqBody, config.getHttpConnectTimeoutMs(), config.getHttpReadTimeoutMs(), config.isAutoReport());
        return resp;
    }


    /**
     * 需要证书的请求
     *
     * @param reqData 向wxpay post的请求数据  Map
     * @return API返回数据
     * @throws Exception
     */
    public String requestWithCert(Map<String, String> reqData) throws Exception {
        String urlSuffix;
        if (config.isUseSandbox()) {
            urlSuffix = WXPayConstants.SANDBOX_ORDERQUERY_URL_SUFFIX;
        } else {
            urlSuffix = WXPayConstants.ORDERQUERY_URL_SUFFIX;
        }
        String msgUUID = reqData.get("nonce_str");
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
        } else if (EncryptionTypeConstants.HMACSHA256.equals(config.getSignType())) {
            reqData.put("sign_type", WXPayConstants.HMACSHA256);
        }
        reqData.put("sign", WXPayUtil.generateSignature(reqData, config.getKey(), config.getSignType()));
        return reqData;
    }


}
