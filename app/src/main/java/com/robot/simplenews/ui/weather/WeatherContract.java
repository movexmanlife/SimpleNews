package com.robot.simplenews.ui.weather;

import com.robot.simplenews.entity.WeatherEntity;
import com.robot.simplenews.ui.base.BasePresenter;
import com.robot.simplenews.ui.base.BaseView;

import java.util.List;

/**
 */
public class WeatherContract {
    public interface View extends BaseView {
        void showProgress();

        void hideProgress();

        void showWeatherLayout();

        void setCity(String city);

        void setToday(String data);

        void setTemperature(String temperature);

        void setWind(String wind);

        void setWeather(String weather);

        void setWeatherImage(int res);

        void setWeatherData(List<WeatherEntity> lists);

        void showErrorToast(String msg);
    }

    public interface Presenter extends BasePresenter<View> {
        void loadWeatherData();
    }
}
