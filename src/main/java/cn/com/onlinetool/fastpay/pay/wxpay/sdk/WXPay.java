package cn.com.onlinetool.fastpay.pay.wxpay.sdk;

import cn.com.onlinetool.fastpay.constants.EncryptionTypeConstants;
import cn.com.onlinetool.fastpay.pay.wxpay.util.WXPayRequestUtil;
import cn.com.onlinetool.fastpay.util.ConverterUtil;
import cn.com.onlinetool.fastpay.pay.wxpay.config.WXPayConfig;
import cn.com.onlinetool.fastpay.pay.wxpay.constants.WXPayConstants;
import cn.com.onlinetool.fastpay.pay.wxpay.util.WXPayUtil;
import cn.com.onlinetool.fastpay.pay.wxpay.constants.WXPayConstants.SignType;

import java.util.Map;

public class WXPay {

    private final WXPayConfig config;
    private final WXPayRequestUtil wxPayRequestUtil;


    public WXPay(final WXPayConfig config) throws Exception {
        this.checkWXPayConfig();
        this.config = config;
        this.wxPayRequestUtil = new WXPayRequestUtil(config);
    }

    /**
     * 检查微信支付配置必传参数是否为空
     * @throws Exception
     */
    private void checkWXPayConfig() throws Exception {
        if (this.config == null) {
            throw new Exception("config is null");
        }
        if (this.config.getAppid() == null || this.config.getAppid().trim().length() == 0) {
            throw new Exception("appid in config is empty");
        }
        if (this.config.getMchId() == null || this.config.getMchId().trim().length() == 0) {
            throw new Exception("appid in config is empty");
        }
        if (this.config.getCertStream() == null) {
            throw new Exception("cert stream in config is empty");
        }
        if (this.config.getWxPayDomain() == null){
            throw new Exception("config.getWXPayDomain() is null");
        }

        if (this.config.getHttpConnectTimeoutMs() < 10) {
            throw new Exception("http connect timeout is too small");
        }
        if (this.config.getHttpReadTimeoutMs() < 10) {
            throw new Exception("http read timeout is too small");
        }

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
        else if (SignType.HMACSHA256.equals(config.getSignType())) {
            reqData.put("sign_type", WXPayConstants.HMACSHA256);
        }
        reqData.put("sign", WXPayUtil.generateSignature(reqData, config.getKey(), config.getSignType()));
        return reqData;
    }

    /**
     * 判断xml数据的sign是否有效，必须包含sign字段，否则返回false。
     *
     * @param reqData 向wxpay post的请求数据
     * @return 签名是否有效
     * @throws Exception
     */
    public boolean isResponseSignatureValid(Map<String, String> reqData) throws Exception {
        // 返回数据的签名方式和请求中给定的签名方式是一致的
        return WXPayUtil.isSignatureValid(reqData, this.config.getKey(), config.getSignType());
    }

    /**
     * 判断支付结果通知中的sign是否有效
     *
     * @param reqData 向wxpay post的请求数据
     * @return 签名是否有效
     * @throws Exception
     */
    public boolean isPayResultNotifySignatureValid(Map<String, String> reqData) throws Exception {
        String signTypeInData = reqData.get(WXPayConstants.FIELD_SIGN_TYPE);
        SignType signType;
        if (signTypeInData == null) {
            signType = SignType.MD5;
        }
        else {
            signTypeInData = signTypeInData.trim();
            if (signTypeInData.length() == 0) {
                signType = SignType.MD5;
            }
            else if (WXPayConstants.MD5.equals(signTypeInData)) {
                signType = SignType.MD5;
            }
            else if (WXPayConstants.HMACSHA256.equals(signTypeInData)) {
                signType = SignType.HMACSHA256;
            }
            else {
                throw new Exception(String.format("Unsupported sign_type: %s", signTypeInData));
            }
        }
        return WXPayUtil.isSignatureValid(reqData, this.config.getKey(), config.getSignType());
    }


    /**
     * 不需要证书的请求
     * @param urlSuffix String
     * @param reqData 向wxpay post的请求数据
     * @param connectTimeoutMs 超时时间，单位是毫秒
     * @param readTimeoutMs 超时时间，单位是毫秒
     * @return API返回数据
     * @throws Exception
     */
    public String requestWithoutCert(String urlSuffix, Map<String, String> reqData,
                                     int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String msgUUID = reqData.get("nonce_str");
        String reqBody = ConverterUtil.mapToXml(reqData);

        String resp = this.wxPayRequestUtil.requestWithoutCert(urlSuffix, msgUUID, reqBody, connectTimeoutMs, readTimeoutMs, config.isAutoReport());
        return resp;
    }


    /**
     * 需要证书的请求
     * @param urlSuffix String
     * @param reqData 向wxpay post的请求数据  Map
     * @param connectTimeoutMs 超时时间，单位是毫秒
     * @param readTimeoutMs 超时时间，单位是毫秒
     * @return API返回数据
     * @throws Exception
     */
    public String requestWithCert(String urlSuffix, Map<String, String> reqData,
                                  int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String msgUUID= reqData.get("nonce_str");
        String reqBody = ConverterUtil.mapToXml(reqData);

        String resp = this.wxPayRequestUtil.requestWithCert(urlSuffix, msgUUID, reqBody, connectTimeoutMs, readTimeoutMs, config.isAutoReport());
        return resp;
    }

    /**
     * 处理 HTTPS API返回数据，转换成Map对象。return_code为SUCCESS时，验证签名。
     * @param xmlStr API返回的XML格式数据
     * @return Map类型数据
     * @throws Exception
     */
    public Map<String, String> processResponseXml(String xmlStr) throws Exception {
        String RETURN_CODE = "return_code";
        String return_code;
        Map<String, String> respData = ConverterUtil.xmlToMap(xmlStr);
        if (respData.containsKey(RETURN_CODE)) {
            return_code = respData.get(RETURN_CODE);
        }
        else {
            throw new Exception(String.format("No `return_code` in XML: %s", xmlStr));
        }

        if (return_code.equals(WXPayConstants.FAIL)) {
            return respData;
        }
        else if (return_code.equals(WXPayConstants.SUCCESS)) {
           if (this.isResponseSignatureValid(respData)) {
               return respData;
           }
           else {
               throw new Exception(String.format("Invalid sign value in XML: %s", xmlStr));
           }
        }
        else {
            throw new Exception(String.format("return_code value %s is invalid in XML: %s", return_code, xmlStr));
        }
    }

    /**
     * 作用：提交刷卡支付<br>
     * 场景：刷卡支付
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     * @throws Exception
     */
    public Map<String, String> microPay(Map<String, String> reqData) throws Exception {
        return this.microPay(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }


    /**
     * 作用：提交刷卡支付<br>
     * 场景：刷卡支付
     * @param reqData 向wxpay post的请求数据
     * @param connectTimeoutMs 连接超时时间，单位是毫秒
     * @param readTimeoutMs 读超时时间，单位是毫秒
     * @return API返回数据
     * @throws Exception
     */
    public Map<String, String> microPay(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (config.isUseSandbox()) {
            url = WXPayConstants.SANDBOX_MICROPAY_URL_SUFFIX;
        }
        else {
            url = WXPayConstants.MICROPAY_URL_SUFFIX;
        }
        String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml);
    }

    /**
     * 提交刷卡支付，针对软POS，尽可能做成功
     * 内置重试机制，最多60s
     * @param reqData
     * @return
     * @throws Exception
     */
    public Map<String, String> microPayWithPos(Map<String, String> reqData) throws Exception {
        return this.microPayWithPos(reqData, this.config.getHttpConnectTimeoutMs());
    }

    /**
     * 提交刷卡支付，针对软POS，尽可能做成功
     * 内置重试机制，最多60s
     * @param reqData
     * @param connectTimeoutMs
     * @return
     * @throws Exception
     */
    public Map<String, String> microPayWithPos(Map<String, String> reqData, int connectTimeoutMs) throws Exception {
        int remainingTimeMs = 60*1000;
        long startTimestampMs = 0;
        Map<String, String> lastResult = null;
        Exception lastException = null;

        while (true) {
            startTimestampMs = WXPayUtil.getCurrentTimestampMs();
            int readTimeoutMs = remainingTimeMs - connectTimeoutMs;
            if (readTimeoutMs > 1000) {
                try {
                    lastResult = this.microPay(reqData, connectTimeoutMs, readTimeoutMs);
                    String returnCode = lastResult.get("return_code");
                    if (returnCode.equals("SUCCESS")) {
                        String resultCode = lastResult.get("result_code");
                        String errCode = lastResult.get("err_code");
                        if (resultCode.equals("SUCCESS")) {
                            break;
                        }
                        else {
                            // 看错误码，若支付结果未知，则重试提交刷卡支付
                            if (errCode.equals("SYSTEMERROR") || errCode.equals("BANKERROR") || errCode.equals("USERPAYING")) {
                                remainingTimeMs = remainingTimeMs - (int)(WXPayUtil.getCurrentTimestampMs() - startTimestampMs);
                                if (remainingTimeMs <= 100) {
                                    break;
                                }
                                else {
                                    WXPayUtil.getLogger().info("microPayWithPos: try micropay again");
                                    if (remainingTimeMs > 5*1000) {
                                        Thread.sleep(5*1000);
                                    }
                                    else {
                                        Thread.sleep(1*1000);
                                    }
                                    continue;
                                }
                            }
                            else {
                                break;
                            }
                        }
                    }
                    else {
                        break;
                    }
                }
                catch (Exception ex) {
                    lastResult = null;
                    lastException = ex;
                }
            }
            else {
                break;
            }
        }

        if (lastResult == null) {
            throw lastException;
        }
        else {
            return lastResult;
        }
    }



    /**
     * 作用：撤销订单<br>
     * 场景：刷卡支付
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     * @throws Exception
     */
    public Map<String, String> reverse(Map<String, String> reqData) throws Exception {
        return this.reverse(reqData, config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }


    /**
     * 作用：撤销订单<br>
     * 场景：刷卡支付<br>
     * 其他：需要证书
     * @param reqData 向wxpay post的请求数据
     * @param connectTimeoutMs 连接超时时间，单位是毫秒
     * @param readTimeoutMs 读超时时间，单位是毫秒
     * @return API返回数据
     * @throws Exception
     */
    public Map<String, String> reverse(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (config.isUseSandbox()) {
            url = WXPayConstants.SANDBOX_REVERSE_URL_SUFFIX;
        }
        else {
            url = WXPayConstants.REVERSE_URL_SUFFIX;
        }
        String respXml = this.requestWithCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml);
    }




    /**
     * 作用：授权码查询OPENID接口<br>
     * 场景：刷卡支付
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     * @throws Exception
     */
    public Map<String, String> authCodeToOpenid(Map<String, String> reqData) throws Exception {
        return this.authCodeToOpenid(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }


    /**
     * 作用：授权码查询OPENID接口<br>
     * 场景：刷卡支付
     * @param reqData 向wxpay post的请求数据
     * @param connectTimeoutMs 连接超时时间，单位是毫秒
     * @param readTimeoutMs 读超时时间，单位是毫秒
     * @return API返回数据
     * @throws Exception
     */
    public Map<String, String> authCodeToOpenid(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (config.isUseSandbox()) {
            url = WXPayConstants.SANDBOX_AUTHCODETOOPENID_URL_SUFFIX;
        }
        else {
            url = WXPayConstants.AUTHCODETOOPENID_URL_SUFFIX;
        }
        String respXml = this.requestWithoutCert(url, this.fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml);
    }


}
