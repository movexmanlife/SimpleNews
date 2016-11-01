package com.robot.simplenews.http;

import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class DecodeResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private static final String RESPONSE_FORMAT_IMAGE_DATA1 = "[{\"title\"";
    private static final String RESPONSE_FORMAT_IMAGE_DATA2 = "\"thumburl\":";
    private static final String RESPONSE_FORMAT_IMAGE_DATA3 = "\"sourceurl\":";

    private static final String RESPONSE_FORMAT_LOCATION_DATA1 = "\"location\":";
    private static final String RESPONSE_FORMAT_LOCATION_DATA2 = "\"city\":";
    private static final String RESPONSE_FORMAT_LOCATION_DATA3 = "\"cityCode\":";
    private static final String RESPONSE_FORMAT_LOCATION_DATA4 = "\"lng\":";
    private static final String RESPONSE_FORMAT_LOCATION_DATA5 = "\"lat\":";

    private static final String RESPONSE_FORMAT_WEATHER_DATA1 = "\"fengxiang\":";
    private static final String RESPONSE_FORMAT_WEATHER_DATA2 = "\"fengli\":";
    private static final String RESPONSE_FORMAT_WEATHER_DATA3 = "\"high\":";
    private static final String RESPONSE_FORMAT_WEATHER_DATA4 = "\"low\":";

    private final TypeAdapter<T> adapter;

    public DecodeResponseBodyConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        // 解密字符串
        // return adapter.fromJson(EncryptUtils.decode(value.string()));
        String content = null;
        String responseContent = value.string();
        if (TextUtils.isEmpty(responseContent)) {
            return adapter.fromJson(content);
        }

        if (responseContent.contains(RESPONSE_FORMAT_IMAGE_DATA1) &&
                responseContent.contains(RESPONSE_FORMAT_IMAGE_DATA2) &&
                responseContent.contains(RESPONSE_FORMAT_IMAGE_DATA3)) { // 图片接口返回的数据
            content = responseContent;
        } else if (responseContent.contains(RESPONSE_FORMAT_LOCATION_DATA1) &&
                responseContent.contains(RESPONSE_FORMAT_LOCATION_DATA2) &&
                responseContent.contains(RESPONSE_FORMAT_LOCATION_DATA3) &&
                responseContent.contains(RESPONSE_FORMAT_LOCATION_DATA4) &&
                responseContent.contains(RESPONSE_FORMAT_LOCATION_DATA5)) { // 定位返回的数据
            content = responseContent;
        } else if (responseContent.contains(RESPONSE_FORMAT_WEATHER_DATA1) &&
                responseContent.contains(RESPONSE_FORMAT_WEATHER_DATA2) &&
                responseContent.contains(RESPONSE_FORMAT_WEATHER_DATA3) &&
                responseContent.contains(RESPONSE_FORMAT_WEATHER_DATA4)) { // 天气返回的数据
            content = getWeatherContent(responseContent);
        } else { // 新闻接口返回的数据
            try {
                JSONObject object = new JSONObject(responseContent);
                Iterator<?> iterator = object.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    if (key != null) {
                        content = object.getString(key);
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return adapter.fromJson(content);
    }

    public static String getWeatherContent(String content) {
        if (TextUtils.isEmpty(content)) {
            return content;
        }
        JsonParser parser = new JsonParser();
        JsonObject jsonObj = parser.parse(content).getAsJsonObject();
        String status = jsonObj.get("status").getAsString();
        if("1000".equals(status)) {
            JsonArray jsonArray = jsonObj.getAsJsonObject("data").getAsJsonArray("forecast");
            return jsonArray.toString();
        }
        return "";
    }
}
