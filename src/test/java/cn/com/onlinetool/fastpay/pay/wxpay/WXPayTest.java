package cn.com.onlinetool.fastpay.pay.wxpay;

import cn.com.onlinetool.fastpay.constants.EncryptionTypeConstants;
import cn.com.onlinetool.fastpay.pay.wxpay.config.WXPayConfig;
import cn.com.onlinetool.fastpay.pay.wxpay.constants.WXPayTypeConstants;
import cn.com.onlinetool.fastpay.pay.wxpay.request.WXPayUnifiedOrderRequest;
import cn.com.onlinetool.fastpay.pay.wxpay.response.WXPaySyncNotifyResponse;
import cn.com.onlinetool.fastpay.pay.wxpay.response.WXPayUnifiedOrderResponse;
import cn.com.onlinetool.fastpay.util.ConverterUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CaseFormat;
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
public class WXPayTest {
    private static WXPay wxPay;
    static {

        String yourAppid = "wx6dfc44874f7d95b1";
        String yourMchId = "1536951601";
        String yourKey = "2B6zpx8tYtOfwUcv8SUJVLP6zBSNeld2";

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
        System.out.println(JSONObject.toJSONString(response));
    }

    @Test
    public void syncNotify() throws Exception {
        Map<String,String> test = new HashMap<>();
        test.put("return_code","200");
        test.put("return_msg","成功");
        String testXml = ConverterUtil.mapToXml(test);
        WXPaySyncNotifyResponse response = wxPay.syncNotify(testXml);
        Assertions.assertEquals("200",response.getReturnCode());
        Assertions.assertEquals("成功",response.getReturnMsg());
    }


}
