package cn.com.onlinetool.fastpay.interceptor.okhttp;

import cn.com.onlinetool.fastpay.util.LoggerUtil;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * @author choice
 * 请求拦截器，此拦截器用于打印日志使用 比如 打印请求耗时等
 * @date 2019-06-19 15:13
 *
 */
public class OkHttpClientLoggingInterceptor implements Interceptor {

    private static final Logger LOGGER = LoggerUtil.getLogger(OkHttpClientLoggingInterceptor.class);
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long startTime = System.nanoTime();
        LOGGER.info(String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        Response response =  chain.proceed(request);

        long endTime = System.nanoTime();
        LOGGER.info(String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (endTime - startTime) / 1e6d, response.headers()));

        return response;
    }
}
