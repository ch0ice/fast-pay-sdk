package cn.com.onlinetool.fastpay.annotations.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author choice
 * @description:
 * @date 2019-08-14 17:12
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface MethodType {
    /**
     * 支付类型，用于确定调用方法是否支持当前支付类型
     * @return
     */
    String[] value();
}
