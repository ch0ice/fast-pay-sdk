package cn.com.onlinetool.fastpay.util;

import okhttp3.Call;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author choice
 * okHttp异步请求回调方法定义
 * @date 2019-06-19 14:33
 *
 */
public interface OkHttpAsyncNotify {
    /**
     * 请求失败时回调
     * @param call
     * @param e
     */
    public void onFailure(Call call, IOException e);

    /**
     * 请求成功时回调
     * @param call
     * @param e
     */
    public void onSuccess(Call call, Response e);
}
