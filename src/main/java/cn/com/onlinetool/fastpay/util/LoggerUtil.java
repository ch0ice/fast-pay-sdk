package cn.com.onlinetool.fastpay.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author choice
 * 日志工具类
 * @date 2019-06-19 14:15
 *
 */
public final class LoggerUtil {

    public static Logger getLogger(Class cls){
        return LoggerFactory.getLogger("fastPaySDK: " + cls.getCanonicalName());
    }
}
