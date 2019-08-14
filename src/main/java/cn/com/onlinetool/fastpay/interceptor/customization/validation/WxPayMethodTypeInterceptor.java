package cn.com.onlinetool.fastpay.interceptor.customization.validation;

import cn.com.onlinetool.fastpay.annotations.validation.MethodType;
import cn.com.onlinetool.fastpay.annotations.validation.ValidationException;
import cn.com.onlinetool.fastpay.interceptor.customization.Interceptor;
import cn.com.onlinetool.fastpay.interceptor.customization.InterceptorHandler;
import cn.com.onlinetool.fastpay.pay.wxpay.WXPay;
import cn.com.onlinetool.fastpay.pay.wxpay.config.WXPayConfig;

import java.lang.reflect.Method;

/**
 * @author choice
 * 微信方法类型校验拦截器
 * @date 2019-08-14 17:45
 *
 */
public class WxPayMethodTypeInterceptor implements Interceptor {



    @Override
    public void before(Object target, Object proxy, Method method, Object[] args) throws Exception {
        methodType(target,method);

    }

    @Override
    public void after(Object target, Object proxy, Method method, Object[] args) {

    }

    private void methodType(Object target,Method method) throws Exception{
        MethodType methodType = method.getAnnotation(MethodType.class);
        if (methodType == null) {
            return;
        }
        WXPay wxPay = (WXPay)target;
        WXPayConfig config = wxPay.config();
        String tradeType = config.getTradeType();

        String[] types = methodType.value();
        int n = 0;
        for( String type : types){
            if(type.equals(tradeType)){
                n++;
            }
        }
        if(n == 0){
            throw new ValidationException(method.getName() + " 方法不支持此支付类型。");
        }
    }

}
