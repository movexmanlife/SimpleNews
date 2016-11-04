package com.robot.simplenews.ui.weather;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.robot.simplenews.R;
import com.robot.simplenews.entity.WeatherEntity;
import com.robot.simplenews.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 天气预报页面
 */
public class WeatherFragment extends BaseFragment implements WeatherContract.View {

    private WeatherPresenter mWeatherPresenter;
    @BindView(R.id.today)
    TextView mTodayTV;
    @BindView(R.id.weatherImage)
    ImageView mTodayWeatherImage;
    @BindView(R.id.weatherTemp)
    TextView mTodayTemperatureTV;
    @BindView(R.id.wind)
    TextView mTodayWindTV;
    @BindView(R.id.weather)
    TextView mTodayWeatherTV;
    @BindView(R.id.city)
    TextView mCityTV;
    @BindView(R.id.progress)
    ProgressBar mProgressBar;
    @BindView(R.id.weather_layout)
    LinearLayout mWeatherLayout;
    @BindView(R.id.weather_content)
    LinearLayout mWeatherContentLayout;
    @BindView(R.id.root_layout)
    FrameLayout mRootLayout;

    public static WeatherFragment newInstance() {
        Bundle args = new Bundle();
        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherPresenter = new WeatherPresenter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, null);
        ButterKnife.bind(this, view);
        mWeatherPresenter.attachView(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mWeatherPresenter.loadWeatherData();
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showWeatherLayout() {
        mWeatherLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setCity(String city) {
        mCityTV.setText(city);
    }

    @Override
    public void setToday(String data) {
        mTodayTV.setText(data);
    }

    @Override
    public void setTemperature(String temperature) {
        mTodayTemperatureTV.setText(temperature);
    }

    @Override
    public void setWind(String wind) {
        mTodayWindTV.setText(wind);
    }

    @Override
    public void setWeather(String weather) {
        mTodayWeatherTV.setText(weather);
    }

    @Override
    public void setWeatherImage(int res) {
        mTodayWeatherImage.setImageResource(res);
    }

    @Override
    public void setWeatherData(List<WeatherEntity> lists) {
        List<View> adapterList = new ArrayList<View>();
        for (WeatherEntity weatherEntity : lists) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_weather, null, false);

            TextView dateTV = (TextView) view.findViewById(R.id.date);
            ImageView todayWeatherImage = (ImageView) view.findViewById(R.id.weatherImage);
            TextView todayTemperatureTV = (TextView) view.findViewById(R.id.weatherTemp);
            TextView todayWindTV = (TextView) view.findViewById(R.id.wind);
            TextView todayWeatherTV = (TextView) view.findViewById(R.id.weather);

            dateTV.setText(weatherEntity.getWeek());
            todayTemperatureTV.setText(weatherEntity.getTemperature());
            todayWindTV.setText(weatherEntity.getWind());
            todayWeatherTV.setText(weatherEntity.getWeather());
            todayWeatherImage.setImageResource(weatherEntity.getImageRes());
            mWeatherContentLayout.addView(view);
            adapterList.add(view);
        }
    }

    @Override
    public void showErrorToast(String msg) {
        Snackbar.make(getActivity().findViewById(R.id.drawer_layout), msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mWeatherPresenter.detachView();
    }
}
