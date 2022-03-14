package com.laogeli.user.utils;

import cn.hutool.core.util.StrUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
*
* @Desecription: OK HTTP 工具类
* @Author: wang
* @Date: 2021/8/24 14:50
*/
public class OkHttpUtils {

    //HTTP请求器
    private CloseableHttpClient httpClient;

    //连接超时时间，默认10秒
    private int socketTimeout = 10000;

    //传输超时时间，默认30秒
    private int connectTimeout = 30000;

    //请求器的配置
    private RequestConfig requestConfig;


    private final static OkHttpClient client = new OkHttpClient().newBuilder()
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    public static JsonObject doGetRetJson(String url) throws IOException {
        return doGetRetJsonElement(url).getAsJsonObject();
    }

    public static JsonObject doGetRetJson(String url, Map<String, String> params) throws IOException {
        String str = "?";
        for (String key : params.keySet()) {
            str += key + "=" + params.get(key) + "&";
        }
        String substring = str.substring(0, str.length() - 1);
        return doGetRetJsonElement(url + substring).getAsJsonObject();
    }

    public static JsonElement doGetRetJsonElement(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        return sendSyncRequestRetJsonElement(request);
    }

    public static JsonObject doPostRetJson(String url, String jsonBody) throws IOException {
        return doPostRetJsonElement(url, jsonBody).getAsJsonObject();
    }

    public static JsonElement doPostRetJsonElement(String url, String jsonBody) throws IOException {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        return sendSyncRequestRetJsonElement(request);
    }

    public static JsonElement jlfDoPostRetJsonElement(String url, String jsonBody, String token) throws IOException {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
        Request request = new Request.Builder().header("Authorization", "Bearer " + token).url(url).post(requestBody).build();
        return sendSyncRequestRetJsonElement(request);
    }

    public static String doPostRetJsonString(String url, String jsonBody) throws IOException {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        return sendSyncRequestRetString(request);
    }

    public static String doPostRetString(String url, String textBody) throws IOException {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"), textBody);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        return sendSyncRequestRetString(request);
    }

    public static void doPost(String url, Map<String, String> params) throws IOException {
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (String key : params.keySet()) {
            formBuilder.add(key, params.get(key));
        }
        Request request = new Request.Builder().url(url).post(formBuilder.build()).build();
        sendSyncRequestRetString(request);
    }

    public static JsonObject doPostRetJson(String url, Map<String, String> params) throws IOException {
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (String key : params.keySet()) {
            formBuilder.add(key, params.get(key));
        }
        Request request = new Request.Builder().url(url).post(formBuilder.build()).build();
        return sendSyncRequestRetJson(request);
    }

    /**
     * post请求返回String
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static String doPostResString(String url, Map<String, String> params) throws IOException {
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (String key : params.keySet()) {
            formBuilder.add(key, params.get(key));
        }
        Request request = new Request.Builder().url(url).post(formBuilder.build()).build();
        return sendSyncRequestRetString(request);
    }

    public static JsonElement doPostRetJsonFile(String url, Map<String, Object> params) throws IOException {
        MultipartBody.Builder formBuilder = new MultipartBody.Builder();
        formBuilder.setType(MultipartBody.FORM);
        params.forEach((key, value) -> {
            if (value instanceof File) {
                File file = (File) value;
                formBuilder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse("application/octet-stream; charset=utf-8"), file));
            } else {
                formBuilder.addFormDataPart(key, value.toString());
            }
        });
        Request request = new Request.Builder().url(url).post(formBuilder.build()).build();
        return sendSyncRequestRetJson(request);
    }

    public static JsonObject doPostRetJsonForUploadFile(String url, Map<String, Object> params) throws IOException {
        MultipartBody.Builder formBuilder = new MultipartBody.Builder();
        formBuilder.setType(MultipartBody.FORM);
        params.forEach((key, value) -> {
            if (value instanceof File) {
                File file = (File) value;
                formBuilder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse("application/pdf; charset=utf-8"), file));
            } else {
                formBuilder.addFormDataPart(key, value.toString());
            }
        });
        Request request = new Request.Builder().url(url).post(formBuilder.build()).build();
        return sendSyncRequestRetJson(request);
    }

    private static JsonObject sendSyncRequestRetJson(Request request) throws IOException {
        return sendSyncRequestRetJsonElement(request).getAsJsonObject();
    }

    private static JsonElement sendSyncRequestRetJsonElement(Request request) throws IOException {
        return GsonUtils.toJsonElement(sendSyncRequestRetString(request));
    }

    /**
     * post请求发送xml报文
     *
     * @param url    ---请求地址
     * @param xmlStr --- xml报文
     * @return
     */
    public static String postXml(String url, String xmlStr) {
        RequestBody body = RequestBody.create(MediaType.parse("application/xml"), xmlStr);
        Request requestOk = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response;
        try {
            response = new OkHttpClient().newCall(requestOk).execute();
            String jsonString = response.body().string();
            if (response.isSuccessful()) {
                return jsonString;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "";
    }

    private static String sendSyncRequestRetString(Request request) throws IOException {
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("请求出错，出错信息：" + response.message());
        }
        String retString = response.body().string();
        if (StrUtil.isEmpty(retString)) {
            return "";
        }
        return retString;
    }
}
