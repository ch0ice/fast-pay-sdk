package cn.com.onlinetool.fastpay;

import static org.junit.Assert.assertTrue;
import cn.com.onlinetool.fastpay.pay.wxpay.WXPay;
import cn.com.onlinetool.fastpay.pay.wxpay.WXPayImpl;
import cn.com.onlinetool.fastpay.pay.wxpay.config.WXPayConfig;
import cn.com.onlinetool.fastpay.pay.wxpay.request.WXPayShortUrlRequest;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }


    public static void main(String[] args) throws Exception {
        WXPay wxPay = WXPay.newInstance(new WXPayConfig());
        WXPayShortUrlRequest request = new WXPayShortUrlRequest();
        request.setLongUrl("test");
        wxPay.sayHelloWorld(request);

        WXPay wxPay1 = new WXPayImpl(new WXPayConfig());
        wxPay1.sayHelloWorld(request);
    }
}
