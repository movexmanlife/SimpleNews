package com.robot.simplenews.http;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Http请求参数拦截器
 * （1）添加和获取Http的Header参数
 * （2）添加公共参数（GET、POST情况下）
 */
class HttpRequestParamsInterceptor implements Interceptor {
    private static final String TAG = HttpRequestParamsInterceptor.class.getName();
    private static final String KEY_REQUEST_PARAM_HEADER_COOKIE = "Cookie";
    private static final String KEY_RESPONSE_PARAM_HEADER_BEAN = "header";
    private static final String PARAM_SERVICE = "service";
    private static final String PARAM_SIGN = "sign";
    private static final String POST = "POST";
    private static final String GET = "GET";
    private final HttpHelper mRequestHelper;

    public HttpRequestParamsInterceptor(HttpHelper requestHelper) {
        mRequestHelper = requestHelper;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        // 添加Header参数
        Request.Builder newRequestBuilder = addRequestHeaderParams(originalRequest);
        // 添加请求公共参数
        Request newRequest = addRequestParams(originalRequest, newRequestBuilder);
        Response response = chain.proceed(newRequest);
        // 获取Header参数
        getRequestHeaderParams(response);
        return response;
    }

    /**
     * 添加Header参数
     *
     * @param originalRequest
     * @return
     */
    private Request.Builder addRequestHeaderParams(Request originalRequest) {
        Request.Builder newRequestBuilder = originalRequest.newBuilder();
        if (!TextUtils.isEmpty(HttpHelper.getJSession_Id()))
            newRequestBuilder.addHeader(KEY_REQUEST_PARAM_HEADER_COOKIE, HttpHelper.getJSession_Id());
        return newRequestBuilder;
    }

    /**
     * 获取Header参数
     *
     * @param response
     */
    private void getRequestHeaderParams(Response response) {
        String headerStr = response.header(KEY_RESPONSE_PARAM_HEADER_BEAN);
        if (!TextUtils.isEmpty(headerStr)) {
            HeaderBean headerParam = new Gson().fromJson(headerStr, HeaderBean.class);
            Logger.d("here is system time ============= " + headerParam.getSysTime());
        }
    }

    /**
     * 添加Request参数
     *
     * @param originalRequest
     * @param requestBuilder
     * @return
     * @throws IOException
     */
    private Request addRequestParams(Request originalRequest, Request.Builder requestBuilder) throws IOException {
        Logger.d("here is method ============= " + originalRequest.method());
        Request newRequest = null;
        if (GET.equalsIgnoreCase(originalRequest.method())) {
            newRequest = addRequestParamsByGET(originalRequest, requestBuilder);
        } else if (POST.equalsIgnoreCase(originalRequest.method())) {
            newRequest = addRequestParamsByPOST(originalRequest, requestBuilder);
        }
        return newRequest;
    }

    /**
     * 基于post请求，根据请求参数，创建sign，然后构建新的请求对象并返回
     *
     * @param originalRequest 原请求对象
     * @param newRequestBuilder
     * @return
     * @throws IOException
     */
    private Request addRequestParamsByPOST(Request originalRequest, Request.Builder newRequestBuilder) throws IOException {
        Map<String, String> paramMap = getParams(originalRequest, mRequestHelper);
        if (paramMap == null)
            return originalRequest;

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        // 将所有参数设置为url的查询参数
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            formBodyBuilder.add(key, value);
        }
        FormBody formBody = formBodyBuilder.build();
        printRequestUrl(originalRequest, paramMap, POST);

        return newRequestBuilder.post(formBody).build();
    }

    /**
     * 基于get请求，根据请求参数，创建sign，然后构建新的请求对象并返回
     *
     * @param originalRequest 原请求对象
     * @param newRequestBuilder
     * @return
     * @throws IOException
     */
    private Request addRequestParamsByGET(Request originalRequest, Request.Builder newRequestBuilder) throws IOException {
        Map<String, String> paramMap = getParams(originalRequest, mRequestHelper);
        if (paramMap == null)
            return originalRequest;

        String originalUrl = originalRequest.url().toString();
        String link = originalUrl.substring(0, originalUrl.indexOf(HttpConst.JSON)) + HttpConst.JSON;
        HttpUrl.Builder newUrlBulider = originalRequest.url().newBuilder(link);

        // 将所有参数设置为url的查询参数
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            newUrlBulider.addQueryParameter(key, value);
        }
        printRequestUrl(originalRequest, paramMap, GET);

        return newRequestBuilder.url(newUrlBulider.build()).build();
    }

    /**
     * 打印请求Url
     */
    private void printRequestUrl(Request originalRequest, Map<String, String> paramMap, String method) {
        if (HttpConst.IS_DEBUG) {
            String totalUrl = "";
            if (GET.equalsIgnoreCase(method)) {
                String originalUrl = originalRequest.url().toString();
                String link = originalUrl.substring(0, originalUrl.indexOf(HttpConst.JSON)) + HttpConst.JSON;
                totalUrl = link + "?";
            } else if (POST.equalsIgnoreCase(method)) {
                totalUrl = originalRequest.url().toString() + "?";
            }

            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                totalUrl += entry.getKey() + "=" + entry.getValue() + "&";
            }
            Logger.d("here is url ============= " + totalUrl);
        }
    }

    /**
     * 创建参数列表，列表中包含所有的请求参数、公共参数和sign
     *
     * @param request
     * @param requestHelper
     * @return
     */
    private Map<String, String> getParams(Request request, HttpHelper requestHelper) {
        String method = request.method();
        Map<String, String> paramMap;
        if (GET.equalsIgnoreCase(method)) {
            paramMap = getParamsByGET(request);
        } else if (POST.equalsIgnoreCase(method)) {
            paramMap = getParamsByPOST(request);
        } else {
            return null;
        }
        String path = request.url().encodedPath();
        // 从url中取出service，若存在，则将service加入参数集合
        String service = path.substring(path.lastIndexOf("/") + 1, path.indexOf("."));
        if (!paramMap.containsKey(PARAM_SERVICE)) {
            paramMap.put(PARAM_SERVICE, service);
        }

        // 将所有公共参数加入参数集合
        paramMap.putAll(requestHelper.getPublicParams());
        // 将所有参数构建成签名
        String sign = SignUtil.buildSign(paramMap);
        // 将签名加入参数列表
        paramMap.put(PARAM_SIGN, sign);
        return paramMap;
    }

    /**
     * GET方法添加参数
     */
    private Map<String, String> getParamsByGET(Request request) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0, size = request.url().querySize(); i < size; i++) {
            String key = request.url().queryParameterName(i);
            String value = request.url().queryParameterValue(i);
            map.put(key, value);
        }
        return map;
    }

    /**
     * POST方法添加参数
     */
    private Map<String, String> getParamsByPOST(Request request) {
        Map<String, String> map = new HashMap<>();
        RequestBody requestBody = request.body();
        // 表单请求体
        if (requestBody instanceof FormBody) {
            FormBody formBody = (FormBody) requestBody;
            for (int i = 0, size = formBody.size(); i < size; i++) {
                String key = formBody.name(i);
                String value = formBody.value(i);
                map.put(key, value);
            }
        }
        // 防止post请求加了@Query注解
        map.putAll(getParamsByGET(request));

        return map;
    }

    private class HeaderBean {
        private String sysTime;

        public String getSysTime() {
            return sysTime;
        }

        public void setSysTime(String sysTime) {
            this.sysTime = sysTime;
        }
    }
}
