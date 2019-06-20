package cn.com.onlinetool.fastpay;

import cn.com.onlinetool.fastpay.util.OkHttpRequestUtil;

/**
 * @author choice
 * @date 2019-06-19 15:36
 *
 */
public class Test {
    public static void main(String[] args) throws Exception {
        String url = "http://wwww.baidu.com";
        OkHttpRequestUtil.syncGetRequest(url,"application/xml; charset=utf-8",null);
    }
}
