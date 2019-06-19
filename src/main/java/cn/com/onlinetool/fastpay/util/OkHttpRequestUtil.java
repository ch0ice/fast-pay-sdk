package cn.com.onlinetool.fastpay.util;

import okhttp3.*;
import org.slf4j.Logger;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author choice
 * okHttp客户端工具
 * @date 2019-06-19 13:53
 *
 */
public final class OkHttpRequestUtil {
    private static final Logger LOGGER = LoggerUtil.getLogger(OkHttpRequestUtil.class);


    /**
     * 异步请求
     * @param url 请求地址
     * @param requestBody 请求数据体
     * @param notify 实现 OkHttpAsyncNotify 接口，会通知执行结果
     * @param maxRetryNum 重试次数
     * @param cert        证书
     * @param keyType     密钥库类型
     * @param partnerId   证书密码
     * @throws Exception
     */
    private static void asyncRequest(String url, String requestBody, OkHttpAsyncNotify notify,int maxRetryNum, InputStream cert, String keyType, String partnerId) throws Exception {
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        Request.Builder request = new Request.Builder()
                .url(url);
        if(null != requestBody){
            request.post(RequestBody.create(mediaType, requestBody));
        }

        //根据条件获取不同的请求客户端 默认获取普通客户端
        OkHttpClient okHttpClient = OkHttpClientUtil.getOkHttpClient();
        if(maxRetryNum > 1 && null != cert){
            okHttpClient = OkHttpClientUtil.getOkHttpClient(maxRetryNum,cert,keyType,partnerId);
        }else if(null != cert){
            okHttpClient = OkHttpClientUtil.getOkHttpClient(cert,keyType,partnerId);
        }else if(maxRetryNum > 1){
            okHttpClient = OkHttpClientUtil.getOkHttpClient(maxRetryNum);
        }

        Call call = okHttpClient.newCall(request.build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LOGGER.error("onFailure:" + e.getMessage());
                if(null != notify){
                    notify.onFailure(call,e);
                }
            }

            @Override
            public void onResponse(Call call, Response res) throws IOException {
                LOGGER.info("onResponse: " + res.body().string());
                if(null != notify){
                    notify.onSuccess(call,res);
                }
            }
        });
    }


    public static void asyncGetRequest(String url, OkHttpAsyncNotify notify) throws Exception {
        asyncRequest(url,null,notify,0,null,null,null);
    }

    public static void asyncPostRequest(String url, String requestBody, OkHttpAsyncNotify notify) throws Exception {
        asyncRequest(url,requestBody,notify,0,null,null,null);
    }










    /**
     构建同步请求
     * @param url 请求地址
     * @param requestBody 请求数据体
     * @param maxRetryNum 重试次数
     * @param cert        证书
     * @param keyType     密钥库类型
     * @param partnerId   证书密码
     * @return
     * @throws Exception
     */
    private static Response syncRequest(String url,String requestBody,int maxRetryNum, InputStream cert, String keyType, String partnerId) throws Exception {
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        Request.Builder request = new Request.Builder()
                .url(url);
        if(null != requestBody){
            request.post(RequestBody.create(mediaType, requestBody));
        }
        //根据条件获取不同的请求客户端 默认获取普通客户端
        OkHttpClient okHttpClient = OkHttpClientUtil.getOkHttpClient();
        if(maxRetryNum > 1 && null != cert){
            okHttpClient = OkHttpClientUtil.getOkHttpClient(maxRetryNum,cert,keyType,partnerId);
        }else if(null != cert){
            okHttpClient = OkHttpClientUtil.getOkHttpClient(cert,keyType,partnerId);
        }else if(maxRetryNum > 1){
            okHttpClient = OkHttpClientUtil.getOkHttpClient(maxRetryNum);
        }

        Response res = null;
        try {
            res = okHttpClient.newCall(request.build()).execute();
            LOGGER.info("onResponse: " + res.body().string());
        } catch (IOException e) {
            LOGGER.error("onFailure:" + e.getMessage());
        }
        return res;
    }


    public static Response syncGetRequest(String url) throws Exception {
        return syncRequest(url,null,0,null,null,null);
    }


    public static Response syncPostRequest(String url,String requestBody) throws Exception {
        return syncRequest(url,requestBody,0,null,null,null);
    }


    public static Response syncPostRequest(String url,String requestBody,int maxRetry) throws Exception {
        return syncRequest(url,requestBody,maxRetry,null,null,null);
    }


    public static Response syncPostRequest(String url,String requestBody,InputStream cert, String keyType, String partnerId) throws Exception {
        return syncRequest(url,requestBody,0,cert,keyType,partnerId);
    }


    public static Response syncPostRequest(String url,String requestBody,int maxRetryNum, InputStream cert, String keyType, String partnerId) throws Exception {
        return syncRequest(url,requestBody,maxRetryNum,cert,keyType,partnerId);
    }




}
