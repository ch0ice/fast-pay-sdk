package cn.com.onlinetool.fastpay.pay.wxpay;

import cn.com.onlinetool.fastpay.constants.EncryptionTypeConstants;
import cn.com.onlinetool.fastpay.pay.wxpay.config.WXPayConfig;
import cn.com.onlinetool.fastpay.pay.wxpay.constants.WXPayTypeConstants;
import cn.com.onlinetool.fastpay.pay.wxpay.request.WXPayGeneratePaySignatureRequest;
import cn.com.onlinetool.fastpay.pay.wxpay.request.WXPayUnifiedOrderRequest;
import cn.com.onlinetool.fastpay.pay.wxpay.response.WXPayGeneratePaySignatureResponse;
import cn.com.onlinetool.fastpay.pay.wxpay.response.WXPaySyncNotifyResponse;
import cn.com.onlinetool.fastpay.pay.wxpay.response.WXPayUnifiedOrderResponse;
import cn.com.onlinetool.fastpay.util.ConverterUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;

/**
 * @author choice
 * 测试微信支付API
 * @date 2019-06-20 18:36
 *
 */
@Slf4j
public class WXPayTest {
    private static WXPay wxPay;
    static {

        String yourAppid = "";
        String yourMchId = "";
        String yourKey = "";

        WXPayConfig wxPayConfig = new WXPayConfig();
        wxPayConfig.setAppid(yourAppid);
        wxPayConfig.setMchId(yourMchId);
        wxPayConfig.setKey(yourKey);
        wxPayConfig.setSignType(EncryptionTypeConstants.HMACSHA256);
        wxPayConfig.setTradeType(WXPayTypeConstants.JSAPI);
        try {
            wxPay = WXPay.newInstance(wxPayConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void unifiedOrderTest() throws Exception {
        WXPayUnifiedOrderRequest request = new WXPayUnifiedOrderRequest();
        request.setBody("test");
        request.setTotalFee(1);
        request.setOutTradeNo("172394718902347");
        request.setSpbillCreateIp("127.0.0.1");
        request.setNotifyUrl("test");
        request.setOpenid("5NTCABT9qhF09eS9KtjaHA==");

        WXPayUnifiedOrderResponse response = wxPay.unifiedOrder(request);
        System.out.println("统一下单返回:\n" + JSONObject.toJSONString(response));
    }

    @Test
    public void syncNotifyParseTest() throws Exception {
        Map<String,String> test = new HashMap<>();
        test.put("return_code","200");
        test.put("return_msg","成功");
        String testXml = ConverterUtil.mapToXml(test);
        WXPaySyncNotifyResponse response = wxPay.syncNotify(testXml);
        Assertions.assertEquals("200",response.getReturnCode());
        Assertions.assertEquals("成功",response.getReturnMsg());
        System.out.println("异步回调参数解析返回:\n" + JSONObject.toJSONString(response));
    }

    @Test
    public void generatePaySignatureTest() throws Exception {
        WXPayGeneratePaySignatureRequest generatePaySignatureRequest = new WXPayGeneratePaySignatureRequest();
        generatePaySignatureRequest.setNonceStr("nonceStr");
        generatePaySignatureRequest.setPrepayId("PrepayId");
        WXPayGeneratePaySignatureResponse response = wxPay.generatePaySignature(generatePaySignatureRequest);
        System.out.println("支付签名生成返回:\n" + JSONObject.toJSONString(response));
    }

//    @Test
//    public void orderQueryTest() throws Exception {
//        WXPayOrderQueryRequest request =new WXPayOrderQueryRequest();
//        request.setOutTradeNo();
//        System.out.println("订单查询:\n" + JSONObject.toJSONString(response));
//    }



}
