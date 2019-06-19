package cn.com.onlinetool.fastpay.exception;

import cn.com.onlinetool.fastpay.pay.wxpay.enums.FastPayExceptionEnum;

/**
 * @author choice
 * @description: 自定义异常处理
 * @date 2019-06-12 10:57
 *
 */
public class FastPayException extends Exception {

    public FastPayException(FastPayExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMsg());
    }

    public FastPayException(String errMsg) {
        super(errMsg);
    }
}
