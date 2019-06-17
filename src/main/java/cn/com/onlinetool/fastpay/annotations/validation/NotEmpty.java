package cn.com.onlinetool.fastpay.annotations.validation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author choice
 * 校验一个字段是否为空
 * @date 2019-06-15 19:53
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface NotEmpty {

}
