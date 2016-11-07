package com.robot.simplenews.ui.weather;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.robot.simplenews.R;
import com.robot.simplenews.api.news.NewsApi;
import com.robot.simplenews.entity.LocationCityEntity;
import com.robot.simplenews.entity.WeatherEntity;
import com.robot.simplenews.util.LogUtil;
import com.robot.simplenews.util.NetworkUtil;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.List;

import rx.Subscription;
import rx.functions.Action1;

/**
 */
public class WeatherPresenter implements WeatherContract.Presenter {
    private static final String TAG = WeatherPresenter.class.getSimpleName();
    private RxFragment mRxFragment;
    private Context mContext;
    private WeatherContract.View mWeatherView;

    public WeatherPresenter(RxFragment rxFragment) {
        this.mRxFragment = rxFragment;
        this.mContext = mRxFragment.getActivity();
    }

    @Override
    public void attachView(@NonNull WeatherContract.View view) {
        mWeatherView = view;
    }

    @Override
    public void detachView() {
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
        NewsApi.getInstance().getWeather(cityName)
                .compose((mRxFragment).<List<WeatherEntity>>bindUntilEvent(FragmentEvent.DESTROY))
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
        /**
         * java.lang.SecurityException: ConnectivityService: Neither user 10092 nor current process has android.permission.ACCESS_NETWORK_STATE.
         * 这里需要如下的权限：
         * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
         * Google将权限分为两类：
         * 一类是Normal Permissions，这类权限一般不涉及用户隐私，是不需要用户进行授权的，比如手机震动、访问网络等；
         * 另一类是Dangerous Permission，一般是涉及到用户隐私的，需要用户进行授权，比如读取sdcard、访问通讯录等。
         *
         * LocationManager.NETWORK_PROVIDER这个权限无需专门检测，只需在manifast.xml文件中配置即可。
         */
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null) {
            mWeatherView.showErrorToast(mContext.getString(R.string.get_location_failed));
            return;
        }
        double latitude = location.getLatitude(); //经度
        double longitude = location.getLongitude(); //纬度
        NewsApi.getInstance().getLocation(latitude, longitude)
                .compose((mRxFragment).<LocationCityEntity>bindUntilEvent(FragmentEvent.DESTROY))
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
        mWeatherView.showErrorToast(mContext.getString(R.string.get_location_failed));
        mWeatherView.setCity(LocationCityEntity.DEFAULT_CITY);
        loadWeatherData(LocationCityEntity.DEFAULT_CITY);
    }
}
