package cn.com.onlinetool.fastpay.interceptor.okhttp;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author choice
 * okhttp 重试拦截器，用于请求失败时 重新尝试请求
 * @date 2019-06-19 16:02
 *
 */
@Slf4j
public class OkHttpRetryInterceptor implements Interceptor {
    private int maxRetryNum = 0;
    private int retryNum = 0;
    public OkHttpRetryInterceptor(){
        super();
    }

    public OkHttpRetryInterceptor(int maxRetryNum){
        this.maxRetryNum = maxRetryNum;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        log.info("retryNum = {}",retryNum);
        Response response = chain.proceed(request);
        while (!response.isSuccessful() && retryNum < maxRetryNum) {
            retryNum++;
            log.info("retryNum = {}",retryNum);
            response = chain.proceed(request);
        }
        return response;
    }
}
