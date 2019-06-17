package cn.com.onlinetool.fastpay.interceptor;

import java.lang.reflect.Method;

/**
 * @author choice
 * 自定义拦截器接口
 * @date 2019-06-17 16:30
 *
 */
public interface Interceptor {
    /**
     * invoke 之前调用
     * @param proxy
     * @param target
     * @param method
     * @param args
     */
    void before(Object proxy, Object target, Method method, Object[] args) throws Exception;

    /**
     * invoke 之后调用
     * @param proxy
     * @param target
     * @param method
     * @param args
     */
    void after(Object proxy, Object target, Method method, Object[] args);

}
