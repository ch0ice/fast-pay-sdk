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
     * @param contentType 内容类型
     * @param notify 实现 OkHttpAsyncNotify 接口，会通知执行结果
     * @param retryNum 重试次数
     * @param cert        证书
     * @param keyType     密钥库类型
     * @param partnerId   证书密码
     * @throws Exception
     */
    private static void asyncRequest(String url, String requestBody, String contentType,String userAgent,OkHttpAsyncNotify notify,int retryNum, InputStream cert, String keyType, String partnerId) throws Exception {
        MediaType mediaType = MediaType.parse(contentType);
        Request.Builder request = new Request.Builder()
                .url(url);
        request.addHeader("Content-Type", "text/xml");
        if(null != requestBody){
            request.post(RequestBody.create(mediaType, requestBody));
        }
        if(null != userAgent){
            request.addHeader("User-Agent",userAgent);
        }

        //根据条件获取不同的请求客户端 默认获取普通客户端
        OkHttpClient okHttpClient = OkHttpClientUtil.getOkHttpClient();
        if(retryNum > 1 && null != cert){
            okHttpClient = OkHttpClientUtil.getOkHttpClient(retryNum,cert,keyType,partnerId);
        }else if(null != cert){
            okHttpClient = OkHttpClientUtil.getOkHttpClient(cert,keyType,partnerId);
        }else if(retryNum > 1){
            okHttpClient = OkHttpClientUtil.getOkHttpClient(retryNum);
        }

        Call call = okHttpClient.newCall(request.build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(null != notify){
                    notify.onFailure(call,e);
                }
            }

            @Override
            public void onResponse(Call call, Response res) throws IOException {
                if(null != notify){
                    notify.onSuccess(call,res);
                }
            }
        });
    }


    public static void asyncGetRequest(String url, String contentType,String userAgent,OkHttpAsyncNotify notify) throws Exception {
        asyncRequest(url,null,contentType,userAgent,notify,0,null,null,null);
    }

    public static void asyncPostRequest(String url, String contentType,String userAgent,String requestBody, OkHttpAsyncNotify notify) throws Exception {
        asyncRequest(url,requestBody,contentType,userAgent,notify,0,null,null,null);
    }










    /**
     * 同步请求
     * @param url 请求地址
     * @param requestBody 请求数据体
     * @param contentType 内容类型
     * @param retryNum 重试次数
     * @param cert        证书
     * @param keyType     密钥库类型
     * @param partnerId   证书密码
     * @return
     * @throws Exception
     */
    private static Response syncRequest(String url,String requestBody,String contentType,String userAgent,int retryNum, InputStream cert, String keyType, String partnerId) throws Exception {
        MediaType mediaType = MediaType.parse(contentType);
        Request.Builder request = new Request.Builder()
                .url("https://" + url);
        request.addHeader("Content-Type", "text/xml");
        if(null != requestBody){
            request.post(RequestBody.create(mediaType, requestBody));
        }
        if(null != userAgent){
            request.addHeader("User-Agent",userAgent);
        }
        //根据条件获取不同的请求客户端 默认获取普通客户端
        OkHttpClient okHttpClient = OkHttpClientUtil.getOkHttpClient();
        if(retryNum > 1 && null != cert){
            okHttpClient = OkHttpClientUtil.getOkHttpClient(retryNum,cert,keyType,partnerId);
        }else if(null != cert){
            okHttpClient = OkHttpClientUtil.getOkHttpClient(cert,keyType,partnerId);
        }else if(retryNum > 1){
            okHttpClient = OkHttpClientUtil.getOkHttpClient(retryNum);
        }

        Response res = null;
        try {
            res = okHttpClient.newCall(request.build()).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }


    public static Response syncGetRequest(String url,String contentType,String userAgent) throws Exception {
        return syncRequest(url,null,contentType,userAgent,0,null,null,null);
    }


    public static Response syncPostRequest(String url,String requestBody,String contentType,String userAgent) throws Exception {
        return syncRequest(url,requestBody,contentType,userAgent,0,null,null,null);
    }


    public static Response syncPostRequest(String url,String requestBody,String contentType,String userAgent,int retryNum) throws Exception {
        return syncRequest(url,requestBody,contentType,userAgent,retryNum,null,null,null);
    }


    public static Response syncPostRequest(String url,String requestBody,String contentType,String userAgent,InputStream cert, String keyType, String partnerId) throws Exception {
        return syncRequest(url,requestBody,contentType,userAgent,0,cert,keyType,partnerId);
    }


    public static Response syncPostRequest(String url,String requestBody,String contentType,String userAgent,int retryNum, InputStream cert, String keyType, String partnerId) throws Exception {
        return syncRequest(url,requestBody,contentType,userAgent,retryNum,cert,keyType,partnerId);
    }




}
