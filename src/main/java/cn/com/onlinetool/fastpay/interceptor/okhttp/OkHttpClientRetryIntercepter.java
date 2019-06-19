package cn.com.onlinetool.fastpay.interceptor.okhttp;

import cn.com.onlinetool.fastpay.util.LoggerUtil;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * @author choice
 * okhttp 重试拦截器，用于请求失败时 重新尝试请求
 * @date 2019-06-19 16:02
 *
 */
public class OkHttpClientRetryIntercepter implements Interceptor {
    private static final Logger LOGGER = LoggerUtil.getLogger(OkHttpClientLoggingInterceptor.class);

    private int maxRetryNum = 0;
    private int retryNum = 0;
    public OkHttpClientRetryIntercepter(){
        super();
    }

    public OkHttpClientRetryIntercepter(int maxRetryNum){
        this.maxRetryNum = maxRetryNum;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        LOGGER.info("retryNum = {}",retryNum);
        Response response = chain.proceed(request);
        while (!response.isSuccessful() && retryNum < maxRetryNum) {
            retryNum++;
            LOGGER.info("retryNum = {}",retryNum);
            response = chain.proceed(request);
        }
        return response;
    }
}
