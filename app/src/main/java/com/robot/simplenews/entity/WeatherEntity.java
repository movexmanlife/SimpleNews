package com.robot.simplenews.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.robot.simplenews.R;

/**
 * 天气实体类
 */

/**
 * {
 *    "date": "4日星期五",
 *    "fengli": "微风级",
 *    "fengxiang": "无持续风向",
 *    "high": "高温 26℃",
 *    "low": "低温 21℃",
 *    "type": "晴"
 * }
 */
public class WeatherEntity implements Parcelable {
    private static final long serialVersionUID = 1L;
    @SerializedName("high")
    private String high;
    @SerializedName("low")
    private String low;
    @SerializedName("type")
    private String weather;
    @SerializedName("fengxiang")
    private String wind;
    @SerializedName("date")
    private String date;

    /* start----------需要组装的数据---------- */
    private String temperature;
    private String week;
    private int imageRes;
    /* end----------需要组装的数据---------- */

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getTemperature() {
        if (!TextUtils.isEmpty(high) || !TextUtils.isEmpty(low)) {
            temperature = high + " " + low;
        }
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWeek() {
        if (!TextUtils.isEmpty(date)) {
            week = date.substring(date.length() - 3);
        }
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImageRes() {
        if (TextUtils.equals(weather, "多云") || TextUtils.equals(weather, "多云转阴") || TextUtils.equals(weather, "多云转晴")) {
            return R.drawable.biz_plugin_weather_duoyun;
        } else if (TextUtils.equals(weather, "中雨") || TextUtils.equals(weather, "中到大雨")) {
            return R.drawable.biz_plugin_weather_zhongyu;
        } else if (TextUtils.equals(weather, "雷阵雨")) {
            return R.drawable.biz_plugin_weather_leizhenyu;
        } else if (TextUtils.equals(weather, "阵雨") || TextUtils.equals(weather, "阵雨转多云")) {
            return R.drawable.biz_plugin_weather_zhenyu;
        } else if (TextUtils.equals(weather, "暴雪")) {
            return R.drawable.biz_plugin_weather_baoxue;
        } else if (TextUtils.equals(weather, "暴雨")) {
            return R.drawable.biz_plugin_weather_baoyu;
        } else if (TextUtils.equals(weather, "大暴雨")) {
            return R.drawable.biz_plugin_weather_dabaoyu;
        } else if (TextUtils.equals(weather, "大雪")) {
            return R.drawable.biz_plugin_weather_daxue;
        } else if (TextUtils.equals(weather, "大雨") || TextUtils.equals(weather, "大雨转中雨")) {
            return R.drawable.biz_plugin_weather_dayu;
        } else if (TextUtils.equals(weather, "雷阵雨冰雹")) {
            return R.drawable.biz_plugin_weather_leizhenyubingbao;
        } else if (TextUtils.equals(weather, "晴")) {
            return R.drawable.biz_plugin_weather_qing;
        } else if (TextUtils.equals(weather, "沙尘暴")) {
            return R.drawable.biz_plugin_weather_shachenbao;
        } else if (TextUtils.equals(weather, "特大暴雨")) {
            return R.drawable.biz_plugin_weather_tedabaoyu;
        } else if (TextUtils.equals(weather, "雾") || TextUtils.equals(weather, "雾霾")) {
            return R.drawable.biz_plugin_weather_wu;
        } else if (TextUtils.equals(weather, "小雪")) {
            return R.drawable.biz_plugin_weather_xiaoxue;
        } else if (TextUtils.equals(weather, "小雨")) {
            return R.drawable.biz_plugin_weather_xiaoyu;
        } else if (TextUtils.equals(weather, "阴")) {
            return R.drawable.biz_plugin_weather_yin;
        } else if (TextUtils.equals(weather, "雨夹雪")) {
            return R.drawable.biz_plugin_weather_yujiaxue;
        } else if (TextUtils.equals(weather, "阵雪")) {
            return R.drawable.biz_plugin_weather_zhenxue;
        } else if (TextUtils.equals(weather, "中雪")) {
            return R.drawable.biz_plugin_weather_zhongxue;
        } else {
            return R.drawable.biz_plugin_weather_duoyun;
        }
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public WeatherEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.high);
        dest.writeString(this.low);
        dest.writeString(this.weather);
        dest.writeString(this.wind);
        dest.writeString(this.date);
        dest.writeString(this.temperature);
        dest.writeString(this.week);
        dest.writeInt(this.imageRes);
    }

    protected WeatherEntity(Parcel in) {
        this.high = in.readString();
        this.low = in.readString();
        this.weather = in.readString();
        this.wind = in.readString();
        this.date = in.readString();
        this.temperature = in.readString();
        this.week = in.readString();
        this.imageRes = in.readInt();
    }

    public static final Creator<WeatherEntity> CREATOR = new Creator<WeatherEntity>() {
        public WeatherEntity createFromParcel(Parcel source) {
            return new WeatherEntity(source);
        }

        public WeatherEntity[] newArray(int size) {
            return new WeatherEntity[size];
        }
    };
}
