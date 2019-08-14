package cn.com.onlinetool.fastpay.interceptor.customization;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author choice
 * 拦截逻辑处理器
 * @date 2019-06-17 16:34
 */
public class InterceptorHandler implements InvocationHandler {
    /**
     * 真实对象
     */
    private Object target;
    /**
     * 拦截器
     */
    private Class<? extends Interceptor> interceptorClass;


    public InterceptorHandler(Object target, Class<? extends Interceptor> interceptorClass) {
        super();
        this.target = target;
        this.interceptorClass = interceptorClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (interceptorClass == null) {
            return method.invoke(target, args);
        }

        Interceptor interceptor = interceptorClass.newInstance();
        interceptor.before(target, proxy, method, args);
        Object result = method.invoke(target, args);
        interceptor.after(target, proxy, method, args);

        return result;
    }


    /**
     * 绑定拦截器
     * @param target 要绑定的对象
     * @param interceptorClass 拦截器类
     * @param <T>
     * @return
     */
    public static <T> T bind(Object target, Class<? extends Interceptor> interceptorClass){
        return
                (T) Proxy.newProxyInstance(
                        target.getClass().getClassLoader(),
                        target.getClass().getInterfaces(),
                        new InterceptorHandler(target, interceptorClass)
                );
    }

    /**
     * 获取原始对象
     * @param proxy
     * @return
     * @throws Exception
     */
    public static Object getTarget(Object proxy) throws Exception {
        Field field = proxy.getClass().getSuperclass().getDeclaredField("h");
        field.setAccessible(true);
        InterceptorHandler handler = (InterceptorHandler) field.get(proxy);
        Field target = handler.getClass().getDeclaredField("target");
        target.setAccessible(true);
        return target.get(handler);
    }
}
