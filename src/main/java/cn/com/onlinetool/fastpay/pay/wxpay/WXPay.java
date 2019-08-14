package cn.com.onlinetool.fastpay.pay.wxpay;

import cn.com.onlinetool.fastpay.annotations.validation.MethodType;
import cn.com.onlinetool.fastpay.interceptor.customization.InterceptorHandler;
import cn.com.onlinetool.fastpay.interceptor.customization.validation.ValidationInterceptor;
import cn.com.onlinetool.fastpay.interceptor.customization.validation.WxPayCopyPropertiesInterceptor;
import cn.com.onlinetool.fastpay.interceptor.customization.validation.WxPayMethodTypeInterceptor;
import cn.com.onlinetool.fastpay.pay.wxpay.config.WXPayConfig;
import cn.com.onlinetool.fastpay.pay.wxpay.constants.WXPayTypeConstants;
import cn.com.onlinetool.fastpay.pay.wxpay.request.*;
import cn.com.onlinetool.fastpay.pay.wxpay.response.*;

/**
 * @author choice
 * @description:
 * @date 2019-06-17 18:18
 *
 */
public interface WXPay {
    /**
     * 作用：统一下单
     * 场景：公共号支付、扫码支付、APP支付
     * @param request 向wxpay post的请求数据
     * @return API返回数据
     * @throws Exception
     */
    @MethodType({WXPayTypeConstants.JSAPI})
    public WXPayUnifiedOrderResponse unifiedOrder(WXPayUnifiedOrderRequest request) throws Exception ;


    /**
     * 生成支付签名
     * @param request 请求数据
     * @return
     */
    public WXPayGeneratePaySignatureResponse generatePaySignature(WXPayGeneratePaySignatureRequest request) throws Exception ;


    /**
     * 作用：查询订单<br>
     * 场景：刷卡支付、公共号支付、扫码支付、APP支付
     * @return API返回数据
     * @throws Exception
     */
    public WXPayOrderQueryResponse orderQuery(WXPayOrderQueryRequest request) throws Exception ;



    /**
     * 作用：关闭订单<br>
     * 场景：公共号支付、扫码支付、APP支付
     * @param request 向wxpay post的请求数据
     * @return API返回数据
     * @throws Exception
     */
    public WXPayCloseOrderResponse closeOrder(WXPayCloseOrderRequest request) throws Exception ;


    /**
     * 作用：申请退款<br>
     * 场景：刷卡支付、公共号支付、扫码支付、APP支付<br>
     * 其他：需要证书
     * @param request 向wxpay post的请求数据
     * @return API返回数据
     * @throws Exception
     */
    public WXPayRefundResponse refund(WXPayRefundRequest request) throws Exception ;

    /**
     * 作用：退款查询<br>
     * 场景：刷卡支付、公共号支付、扫码支付、APP支付
     * @param request 向wxpay post的请求数据
     * @return API返回数据
     * @throws Exception
     */
    public WXPayRefundQueryResponse refundQuery(WXPayRefundQueryRequest request) throws Exception ;


    /**
     * 作用：对账单下载<br>
     * 场景：刷卡支付、公共号支付、扫码支付、APP支付<br>
     * 其他：无论是否成功都返回Map。若成功，返回的Map中含有return_code、return_msg、data，
     *      其中return_code为`SUCCESS`，data为对账单数据。
     * @return 经过封装的API返回数据
     * @throws Exception
     */
    public WXPayDownloadBillResponse downloadBill(WXPayDownloadBillRequest request) throws Exception ;



    /**
     * 作用：转换短链接<br>
     * 场景：刷卡支付、扫码支付
     * @param request 向wxpay post的请求数据
     * @return API返回数据
     * @throws Exception
     */
    public WXPayShortUrlResponse shortUrl(WXPayShortUrlRequest request) throws Exception ;


    /**
     * 作用：交易保障<br>
     * 场景：刷卡支付、公共号支付、扫码支付、APP支付
     * @param request 向wxpay post的请求数据
     * @return API返回数据
     * @throws Exception
     */
    public WXPayReportResponse report(WXPayReportRequest request) throws Exception ;

    /**
     * 作用：异步回调信息解析
     * 场景：用户自己实现异步回调接口，接收到回调信息后可以调用次函数进行解析
     * @param reqXml 微信异步回调的原始xml
     * @return
     */
    public WXPaySyncNotifyResponse syncNotify(String reqXml) throws Exception ;

    /**
     * 获取微信支付配置
     */
    public WXPayConfig config();


    public static WXPay newInstance(final WXPayConfig config) throws Exception {
        WXPay wxPay = new WXPayImpl(config);
        wxPay = InterceptorHandler.bind(wxPay, WxPayMethodTypeInterceptor.class);
        wxPay = InterceptorHandler.bind(wxPay, WxPayCopyPropertiesInterceptor.class);
        wxPay = InterceptorHandler.bind(wxPay, ValidationInterceptor.class);
        return wxPay;
    }
}
