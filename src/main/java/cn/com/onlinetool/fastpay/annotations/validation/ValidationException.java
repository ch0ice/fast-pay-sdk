package cn.com.onlinetool.fastpay.annotations.validation;

/**
 * @author choice
 * 校验逻辑异常
 * @date 2019-06-17 16:04
 *
 */
public class ValidationException extends RuntimeException{
    public ValidationException(String errMsg) {
        super(errMsg);
    }
}
