package com.robot.simplenews.ui.weather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.robot.simplenews.api.news.NewsApi;
import com.robot.simplenews.entity.LocationCityEntity;
import com.robot.simplenews.entity.WeatherEntity;
import com.robot.simplenews.util.LogUtil;
import com.robot.simplenews.util.NetworkUtil;
import com.robot.simplenews.util.RxUtil;

import java.util.List;

import rx.Subscription;
import rx.functions.Action1;

/**
 */
public class WeatherPresenter implements WeatherContract.Presenter {
    private static final String TAG = WeatherPresenter.class.getSimpleName();
    private Context mContext;
    private WeatherContract.View mWeatherView;
    private Subscription mLocationCitySubscription;
    private Subscription mWeatherSubscription;

    public WeatherPresenter(Context context) {
        this.mContext = context;
    }

    @Override
    public void attachView(@NonNull WeatherContract.View view) {
        mWeatherView = view;
    }

    @Override
    public void detachView() {
        RxUtil.unsubscribe(mLocationCitySubscription);
        RxUtil.unsubscribe(mWeatherSubscription);
        mWeatherView = null;
    }

    @Override
    public void loadWeatherData() {
        mWeatherView.showProgress();
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            mWeatherView.hideProgress();
            mWeatherView.showErrorToast("无网络连接");
            return;
        }
        getLocation();
    }

    public void loadWeatherData(String cityName) {
        mWeatherSubscription = NewsApi.getInstance().getWeather(cityName)
                .subscribe(new Action1<List<WeatherEntity>>() {
                    @Override
                    public void call(List<WeatherEntity> weatherEntityList) {
                        if (weatherEntityList != null && weatherEntityList.size() > 0) {
                            WeatherEntity todayWeather = weatherEntityList.remove(0);
                            mWeatherView.setToday(todayWeather.getDate());
                            mWeatherView.setTemperature(todayWeather.getTemperature());
                            mWeatherView.setWeather(todayWeather.getWeather());
                            mWeatherView.setWind(todayWeather.getWind());
                            mWeatherView.setWeatherImage(todayWeather.getImageRes());
                        }
                        mWeatherView.setWeatherData(weatherEntityList);
                        mWeatherView.hideProgress();
                        mWeatherView.showWeatherLayout();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mWeatherView.hideProgress();
                        mWeatherView.showErrorToast("获取天气数据失败");
                    }
                });
    }

    private void getLocation() {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && mContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                LogUtil.e(TAG, "location failure.");
                return;
            }
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null) {
            LogUtil.e(TAG, "location failure.");
            return;
        }
        double latitude = location.getLatitude(); //经度
        double longitude = location.getLongitude(); //纬度
        mLocationCitySubscription = NewsApi.getInstance().getLocation(latitude, longitude)
                .subscribe(new Action1<LocationCityEntity>() {
                    @Override
                    public void call(LocationCityEntity locationCityEntity) {
                        if (locationCityEntity == null || locationCityEntity.getResult() == null ||
                                locationCityEntity.getResult().getAddressComponent() == null) {
                            locationFailed();
                            return;
                        }

                        String cityName = locationCityEntity.getResult().getAddressComponent().getCity();
                        if (TextUtils.isEmpty(cityName)) {
                            locationFailed();
                            return;
                        }

                        mWeatherView.setCity(cityName);
                        loadWeatherData(cityName);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        locationFailed();
                    }
                });
    }

    private void locationFailed() {
        mWeatherView.showErrorToast("定位失败");
        mWeatherView.setCity(LocationCityEntity.DEFAULT_CITY);
        loadWeatherData(LocationCityEntity.DEFAULT_CITY);
    }
}
