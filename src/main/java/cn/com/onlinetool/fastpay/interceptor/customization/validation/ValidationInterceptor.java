package cn.com.onlinetool.fastpay.interceptor.customization.validation;

import cn.com.onlinetool.fastpay.annotations.validation.NotEmpty;
import cn.com.onlinetool.fastpay.annotations.validation.Validation;
import cn.com.onlinetool.fastpay.annotations.validation.ValidationException;
import cn.com.onlinetool.fastpay.interceptor.customization.Interceptor;

import java.lang.Object;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author choice
 * 参数校验拦截器
 * @date 2019-06-17 16:28
 *
 */
public class ValidationInterceptor implements Interceptor {

    private void validation(Object object) throws Exception {
        Validation validation = object.getClass().getAnnotation(Validation.class);
        if(null != validation){
            Class<? extends Object> clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);

                notEmpty(field, object);

                field.setAccessible(false);
            }
        }

    }

    private void notEmpty(Field field, Object object) throws Exception {
        NotEmpty notEmpty = field.getAnnotation(NotEmpty.class);
        if (notEmpty == null) {
            return;
        }
        Object value = field.get(object);
        if (null == value || "".equals(value)) {
            throw new ValidationException("字段不能为空：" + field.getName());
        }
    }

    @Override
    public void before(Object target, Object proxy, Method method, Object[] args) throws Exception {
        for(Object arg : args){
            this.validation(arg);
        }
    }

    @Override
    public void after(Object target, Object proxy, Method method, Object[] args) {

    }
}
