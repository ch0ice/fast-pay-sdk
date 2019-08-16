package cn.com.onlinetool.fastpay.interceptor.customization.parameters;

import cn.com.onlinetool.fastpay.interceptor.customization.Interceptor;
import cn.com.onlinetool.fastpay.pay.wxpay.WXPay;
import cn.com.onlinetool.fastpay.pay.wxpay.config.WXPayConfig;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Method;

/**
 * @author choice
 * 复制基本支付参数到请求参数中
 * @date 2019-08-14 22:05
 *
 */
public class WxPayCopyParametersInterceptor implements Interceptor {

    @Override
    public void before(Object target, Object proxy, Method method, Object[] args) throws Exception {
        WXPay wxPay = (WXPay) target;
        WXPayConfig config = wxPay.config();
        for(Object arg : args){
            BeanUtils.copyProperties(arg,config);
        }

    }

    @Override
    public void after(Object target, Object proxy, Method method, Object[] args) {

    }


}
