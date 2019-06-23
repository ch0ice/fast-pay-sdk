package cn.com.onlinetool.fastpay.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.BufferedSink;
import org.junit.Test;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

/**
 * @author choice
 * @date 2019-06-19 11:09
 *
 */
@Slf4j
public class OkHttp3ClientUtilTest {
    private static final OkHttpClient okHttpClient = new OkHttpClient();
    /**
     * 测试 okHttp3 异步请求 GET
     * @return
     */
    @Test
    public void okHttp3AsynGetReqTest(){
        String url = "http://wwww.baidu.com";
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.error("onFailure:" + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response res) throws IOException {
                log.info("onResponse: " + res.body().string());
            }
        });
    }

    /**
     * 测试 okHttp3 同步请求 GET
     * @return
     */
    @Test
    public void okHttp3SyncGetReqTest(){
        String url = "http://wwww.baidu.com";
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response res = null;
        try {
            res = okHttpClient.newCall(request).execute();
            System.out.println("onResponse: " + res.body().string());
        } catch (IOException e) {
            System.out.println("onFailure: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 测试 okHttp3 异步请求 POST String
     * @return
     */
    @Test
    public void okHttp3AsynPostStringReqTest(){
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        String requestBody = "I am Jdqm.";
        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(mediaType, requestBody))
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("onFailure: " + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response res) throws IOException {
                System.out.println(res.protocol() + " " +res.code() + " " + res.message());
                Headers headers = res.headers();
                for (int i = 0; i < headers.size(); i++) {
                    System.out.println(headers.name(i) + ":" + headers.value(i));
                }
                System.out.println("onResponse: " + res.body().string());
            }
        });
    }


    /**
     * 测试 okHttp3 同步请求 POST String
     * @return
     */
    @Test
    public void okHttp3SyncPostStringReqTest(){
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        String requestBody = "I am Jdqm.";
        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(mediaType, requestBody))
                .build();
        Response res = null;
        try {
            res = okHttpClient.newCall(request).execute();
            System.out.println(res.protocol() + " " +res.code() + " " + res.message());
            Headers headers = res.headers();
            for (int i = 0; i < headers.size(); i++) {
                System.out.println(headers.name(i) + ":" + headers.value(i));
            }
            System.out.println("onResponse: " + res.body().string());
        } catch (IOException e) {
            System.out.println("onFailure: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试 okHttp3 异步请求 POST Stream
     * @return
     */
    @Test
    public void okHttp3AsynPostStreamReqTest(){
        RequestBody requestBody = new RequestBody() {
            @Nullable
            @Override
            public MediaType contentType() {
                return MediaType.parse("text/x-markdown; charset=utf-8");
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8("I am Jdqm.");
            }
        };

        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("onFailure: " + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response res) throws IOException {
                System.out.println(res.protocol() + " " +res.code() + " " + res.message());
                Headers headers = res.headers();
                for (int i = 0; i < headers.size(); i++) {
                    System.out.println(headers.name(i) + ":" + headers.value(i));
                }
                System.out.println("onResponse: " + res.body().string());
            }
        });
    }

    /**
     * 测试 okHttp3 同步请求 POST Stream
     * @return
     */
    @Test
    public void okHttp3SyncPostStreamReqTest(){
        RequestBody requestBody = new RequestBody() {
            @Nullable
            @Override
            public MediaType contentType() {
                return MediaType.parse("text/x-markdown; charset=utf-8");
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8("I am Jdqm.");
            }
        };

        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(requestBody)
                .build();
        Response res = null;
        try {
            res = okHttpClient.newCall(request).execute();
            System.out.println(res.protocol() + " " +res.code() + " " + res.message());
            Headers headers = res.headers();
            for (int i = 0; i < headers.size(); i++) {
                System.out.println(headers.name(i) + ":" + headers.value(i));
            }
            System.out.println("onResponse: " + res.body().string());
        } catch (IOException e) {
            System.out.println("onFailure: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 测试 okHttp3 异步请求 POST File
     * @return
     */
    @Test
    public void okHttp3AsynPostFileReqTest(){
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        File file = new File("test.md");
        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(mediaType, file))
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("onFailure: " + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response res) throws IOException {
                System.out.println(res.protocol() + " " +res.code() + " " + res.message());
                Headers headers = res.headers();
                for (int i = 0; i < headers.size(); i++) {
                    System.out.println(headers.name(i) + ":" + headers.value(i));
                }
                System.out.println("onResponse: " + res.body().string());
            }
        });
    }

    /**
     * 测试 okHttp3 同步请求 POST File
     * @return
     */
    @Test
    public void okHttp3SyncPostFileReqTest(){
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");

        File file = new File("test.md");
        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(mediaType, file))
                .build();
        Response res = null;
        try {
            res = okHttpClient.newCall(request).execute();
            System.out.println(res.protocol() + " " +res.code() + " " + res.message());
            Headers headers = res.headers();
            for (int i = 0; i < headers.size(); i++) {
                System.out.println(headers.name(i) + ":" + headers.value(i));
            }
            System.out.println("onResponse: " + res.body().string());
        } catch (IOException e) {
            System.out.println("onFailure: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
