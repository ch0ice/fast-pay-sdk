package cn.com.onlinetool.fastpay.pay.wxpay;

import cn.com.onlinetool.fastpay.pay.wxpay.config.WXPayConfig;
import cn.com.onlinetool.fastpay.pay.wxpay.constants.WXPayConstants;
import cn.com.onlinetool.fastpay.pay.wxpay.request.*;
import cn.com.onlinetool.fastpay.pay.wxpay.response.*;
import cn.com.onlinetool.fastpay.pay.wxpay.util.WXPayRequestUtil;
import cn.com.onlinetool.fastpay.pay.wxpay.util.WXPayUtil;
import cn.com.onlinetool.fastpay.util.ConverterUtil;
import com.google.common.base.CaseFormat;
import org.apache.commons.beanutils.BeanUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * @author choice
 * @description: 微信支付API统一入口
 * @date 2019-06-11 14:56
 *
 */
public class WXPayImpl implements WXPay {
    private final WXPayConfig config;
    private final WXPayRequestUtil wxPayRequestUtil;

    public WXPayImpl(final WXPayConfig config) throws Exception {
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
        if (this.config.getCert() == null) {
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


    private Map<String,String> responseSignatureValid(String resposeXml) throws Exception {


        Map<String,String> responseMap = ConverterUtil.xmlToMap(resposeXml);
        final String returnCodeSymbol = "return_code";
        String returnCode;
        if (responseMap.containsKey(returnCodeSymbol)) {
            returnCode = responseMap.get(returnCodeSymbol);
        } else {
            throw new Exception("");
        }

        if (returnCode.equals(WXPayConstants.SUCCESS)) {
            if (!this.isResponseSignatureValid(responseMap)) {
                throw new Exception("微信返回签名验证错误：签名不符");
            }
        }
        return responseMap;
    }

    private boolean isResponseSignatureValid(Map<String, String> reqData) throws Exception {
        // 返回数据的签名方式和请求中给定的签名方式是一致的
        return WXPayUtil.isSignatureValid(reqData, this.config.getKey(), config.getSignType());
    }



    @Override
    public WXPayUnifiedOrderResponse unifiedOrder(WXPayUnifiedOrderRequest request) throws Exception {
        BeanUtils.copyProperties(request,config);
        request.setNonceStr(WXPayUtil.generateNonceStr());
        Map<String,String> requestMap = ConverterUtil.objectToMap(request, CaseFormat.LOWER_CAMEL,CaseFormat.LOWER_UNDERSCORE);
        requestMap.put("sign",WXPayUtil.generateSignature(requestMap,config.getKey(),config.getSignType()));
        String responseXml = wxPayRequestUtil.requestWithoutCert(requestMap).trim();
        return ConverterUtil.mapToObject(this.responseSignatureValid(responseXml),WXPayUnifiedOrderResponse.class,CaseFormat.LOWER_UNDERSCORE,CaseFormat.LOWER_CAMEL);
    }

    @Override
    public WXPayGeneratePaySignatureResponse generatePaySignature(WXPayGeneratePaySignatureRequest request) throws Exception {
        Map<String,String> finalpackage = new HashMap<>();
        finalpackage.put("appId",config.getAppid());
        finalpackage.put("timeStamp", WXPayUtil.getCurrentTimestamp() + "");
        finalpackage.put("nonceStr",request.getNonceStr());
        finalpackage.put("package", "prepay_id="+request.getPrepayId());
        finalpackage.put("signType",config.getSignType());
        finalpackage.put("sign", WXPayUtil.generateSignature(finalpackage, config.getKey(),config.getSignType()));
        return ConverterUtil.mapToObject(finalpackage, WXPayGeneratePaySignatureResponse.class);
    }

    @Override
    public WXPayOrderQueryResponse orderQuery(WXPayOrderQueryRequest request) throws Exception {
        BeanUtils.copyProperties(request,config);
        Map<String,String> requestMap = ConverterUtil.objectToMap(request, CaseFormat.LOWER_CAMEL,CaseFormat.LOWER_UNDERSCORE);
        String responseXml = wxPayRequestUtil.requestWithoutCert(wxPayRequestUtil.fillRequestData(requestMap)).trim();
        return ConverterUtil.mapToObject(this.responseSignatureValid(responseXml),WXPayOrderQueryResponse.class,CaseFormat.LOWER_UNDERSCORE,CaseFormat.LOWER_CAMEL);
    }

    @Override
    public WXPayCloseOrderResponse closeOrder(WXPayCloseOrderRequest request) throws Exception {
        BeanUtils.copyProperties(request,config);
        Map<String,String> requestMap = ConverterUtil.objectToMap(request, CaseFormat.LOWER_CAMEL,CaseFormat.LOWER_UNDERSCORE);
        String responseXml = wxPayRequestUtil.requestWithoutCert(wxPayRequestUtil.fillRequestData(requestMap)).trim();
        return ConverterUtil.mapToObject(this.responseSignatureValid(responseXml),WXPayCloseOrderResponse.class,CaseFormat.LOWER_UNDERSCORE,CaseFormat.LOWER_CAMEL);
    }

    @Override
    public WXPayRefundResponse refund(WXPayRefundRequest request) throws Exception {
        BeanUtils.copyProperties(request,config);
        Map<String,String> requestMap = ConverterUtil.objectToMap(request, CaseFormat.LOWER_CAMEL,CaseFormat.LOWER_UNDERSCORE);
        String responseXml = wxPayRequestUtil.requestWithCert(wxPayRequestUtil.fillRequestData(requestMap)).trim();
        return ConverterUtil.mapToObject(this.responseSignatureValid(responseXml),WXPayRefundResponse.class,CaseFormat.LOWER_UNDERSCORE,CaseFormat.LOWER_CAMEL);

    }

    @Override
    public WXPayRefundQueryResponse refundQuery(WXPayRefundQueryRequest request) throws Exception {
        BeanUtils.copyProperties(request,config);
        Map<String,String> requestMap = ConverterUtil.objectToMap(request, CaseFormat.LOWER_CAMEL,CaseFormat.LOWER_UNDERSCORE);
        String responseXml = wxPayRequestUtil.requestWithoutCert(wxPayRequestUtil.fillRequestData(requestMap)).trim();
        return ConverterUtil.mapToObject(this.responseSignatureValid(responseXml),WXPayRefundQueryResponse.class,CaseFormat.LOWER_UNDERSCORE,CaseFormat.LOWER_CAMEL);

    }

    @Override
    public WXPayDownloadBillResponse downloadBill(WXPayDownloadBillRequest request) throws Exception {
        BeanUtils.copyProperties(request,config);
        Map<String,String> requestMap = ConverterUtil.objectToMap(request, CaseFormat.LOWER_CAMEL,CaseFormat.LOWER_UNDERSCORE);
        String responseXml = wxPayRequestUtil.requestWithoutCert(wxPayRequestUtil.fillRequestData(requestMap)).trim();
        return ConverterUtil.mapToObject(this.responseSignatureValid(responseXml),WXPayDownloadBillResponse.class,CaseFormat.LOWER_UNDERSCORE,CaseFormat.LOWER_CAMEL);
    }

    public WXPayShortUrlResponse shortUrl(WXPayShortUrlRequest request) throws Exception {
        BeanUtils.copyProperties(request,config);
        Map<String,String> requestMap = ConverterUtil.objectToMap(request, CaseFormat.LOWER_CAMEL,CaseFormat.LOWER_UNDERSCORE);
        String responseXml = wxPayRequestUtil.requestWithoutCert(wxPayRequestUtil.fillRequestData(requestMap)).trim();
        return ConverterUtil.mapToObject(this.responseSignatureValid(responseXml),WXPayShortUrlResponse.class,CaseFormat.LOWER_UNDERSCORE,CaseFormat.LOWER_CAMEL);
    }

    @Override
    public WXPayReportResponse report(WXPayReportRequest request) throws Exception {
        BeanUtils.copyProperties(request,config);
        Map<String,String> requestMap = ConverterUtil.objectToMap(request, CaseFormat.LOWER_CAMEL,CaseFormat.LOWER_UNDERSCORE);
        String responseXml = wxPayRequestUtil.requestWithoutCert(wxPayRequestUtil.fillRequestData(requestMap));
        return ConverterUtil.mapToObject(this.responseSignatureValid(responseXml),WXPayReportResponse.class,CaseFormat.LOWER_UNDERSCORE,CaseFormat.LOWER_CAMEL);

    }

    @Override
    public WXPaySyncNotifyResponse syncNotify(String reqXml) throws Exception {
        return ConverterUtil.mapToObject(this.responseSignatureValid(reqXml),WXPaySyncNotifyResponse.class,CaseFormat.LOWER_UNDERSCORE,CaseFormat.LOWER_CAMEL);

    }

    @Override
    public void sayHelloWorld(WXPayShortUrlRequest request){
        System.out.println("Hello World!");
    }



}
