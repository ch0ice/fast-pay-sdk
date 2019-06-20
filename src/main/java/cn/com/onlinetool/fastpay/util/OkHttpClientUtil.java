package cn.com.onlinetool.fastpay.util;

import cn.com.onlinetool.fastpay.interceptor.okhttp.OkHttpRetryInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;

import javax.net.ssl.*;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author choice
 * 获取okhttp client的工具类
 * @date 2019-06-19 19:54
 */
public final class OkHttpClientUtil {
    private static final Logger LOGGER = LoggerUtil.getLogger(OkHttpClientUtil.class);

    /**
     * 用于保存不同类型参数创建的客户端，避免重复创建浪费资源
     */
    private static final HashMap<String, OkHttpClient> OK_HTTP_CLIENT_MAP = new HashMap<>();

    /**
     * 模版方法，不对外暴露
     * 获取带证书和重试功能的客户端
     * @param retryNum 重试次数 为0不重试
     * @param cert        证书
     * @param keyType     密钥库类型
     * @param partnerId   证书密码
     * @return
     * @throws Exception
     */
    private static OkHttpClient buildOkHttpClient(int retryNum, InputStream cert, String keyType, String partnerId) throws Exception {


        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.addInterceptor((new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)));
        builder.addInterceptor(new OkHttpRetryInterceptor(retryNum));
        builder.retryOnConnectionFailure(true);
//            builder.callTimeout(1, TimeUnit.SECONDS)
//                    .connectTimeout(2,TimeUnit.SECONDS)
//                    .readTimeout(2,TimeUnit.SECONDS)
//                    .writeTimeout(2,TimeUnit.SECONDS);
        //证书配置
        if(null != cert){
            KeyStore keystore = KeyStore.getInstance(keyType);
            char[] password = partnerId.toCharArray();
            keystore.load(cert, password);
            SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keystore, password).build();
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keystore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
            builder.sslSocketFactory(sslSocketFactory, trustManager);
        }

        return builder.build();
    }


    /**
     * 获取带证书和重试功能的客户端
     * @param retryNum 重试次数
     * @param cert        证书
     * @param keyType     密钥库类型
     * @param partnerId   证书密码
     * @return
     * @throws Exception
     */
    public static OkHttpClient getOkHttpClient(int retryNum, InputStream cert, String keyType, String partnerId) throws Exception {
        String methodTag = "withCertAndRetry";
        OkHttpClient okHttpClient = OK_HTTP_CLIENT_MAP.get(methodTag);
        if (null != okHttpClient) {
            return okHttpClient;
        }
        okHttpClient = buildOkHttpClient(retryNum,cert,keyType,partnerId);
        OK_HTTP_CLIENT_MAP.put(methodTag, okHttpClient);
        return okHttpClient;
    }

    /**
     * 获取带证书的客户端
     * @param cert        证书
     * @param keyType     密钥库类型
     * @param partnerId   证书密码
     * @return
     * @throws Exception
     */
    public static OkHttpClient getOkHttpClient(InputStream cert, String keyType, String partnerId) throws Exception {
        String methodTag = "withCert";
        OkHttpClient okHttpClient = OK_HTTP_CLIENT_MAP.get(methodTag);
        if (null != okHttpClient) {
            return okHttpClient;
        }
        okHttpClient = buildOkHttpClient(0,cert,keyType,partnerId);
        OK_HTTP_CLIENT_MAP.put(methodTag, okHttpClient);
        return okHttpClient;
    }

    /**
     * 获取带重试功能的客户端
     * @param retryNum 重试次数
     * @return
     * @throws Exception
     */
    public static OkHttpClient getOkHttpClient(int retryNum) throws Exception {
        String methodTag = "withRetry";
        OkHttpClient okHttpClient = OK_HTTP_CLIENT_MAP.get(methodTag);
        if (null != okHttpClient) {
            return okHttpClient;
        }
        okHttpClient = buildOkHttpClient(retryNum,null,null,null);
        OK_HTTP_CLIENT_MAP.put(methodTag, okHttpClient);
        return okHttpClient;
    }

    /**
     * 获取客户端
     * @return
     * @throws Exception
     */
    public static OkHttpClient getOkHttpClient() throws Exception {
        String methodTag = "base";
        OkHttpClient okHttpClient = OK_HTTP_CLIENT_MAP.get(methodTag);
        if (null != okHttpClient) {
            return okHttpClient;
        }
        okHttpClient = buildOkHttpClient(0,null,null,null);
        OK_HTTP_CLIENT_MAP.put(methodTag, okHttpClient);
        return okHttpClient;
    }


}
