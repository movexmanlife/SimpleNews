package com.robot.simplenews.api.news;

import android.content.Context;

import com.robot.simplenews.commons.Urls;
import com.robot.simplenews.entity.ImageEntity;
import com.robot.simplenews.entity.LocationCityEntity;
import com.robot.simplenews.entity.NewsDetailEntity;
import com.robot.simplenews.entity.NewsEntity;
import com.robot.simplenews.entity.WeatherEntity;
import com.robot.simplenews.http.CookieInterceptor;
import com.robot.simplenews.http.DecodeConverterFactory;
import com.robot.simplenews.http.OkHttpClientHelper;
import com.robot.simplenews.ui.news.AllNewsFragment;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewsApi {
    static final String BASE_URL = Urls.HOST;

    private NewsService mNewsService;
    private Context mContext;
    private static NewsApi mNewsApi = new NewsApi();

    private NewsApi() {
        CookieInterceptor cookieInterceptor = new CookieInterceptor();
        Retrofit retrofit =
                new Retrofit.Builder().baseUrl(BASE_URL)
                        .addConverterFactory(DecodeConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .client(new OkHttpClientHelper().provideApiOkHttpClient(cookieInterceptor))
                        .build();
        mNewsService = retrofit.create(NewsService.class);
    }

    public static NewsApi getInstance() {
        return mNewsApi;
    }

    /**
     * 获取新闻列表
     */
    public Observable<List<NewsEntity>> getNewsList(int type, int pageIndex) {
        String[] types = getID(type);
        return mNewsService.getNewsList(types[0], types[1], pageIndex).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取新闻列表
     */
    public Observable<String> getNewsList2() {
        return mNewsService.getNewsList2().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取新闻内容详情
     */
    public Observable<NewsDetailEntity> getNewsDetail(String docId) {
        return mNewsService.getNewsDetail(docId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 图片列表
     */
    public Observable<List<ImageEntity>> getImageList() {
        return mNewsService.getImageList(Urls.IMAGES_URL).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 定位城市
     */
    public Observable<LocationCityEntity> getLocation(double latitude, double longitude) {
        String location = String.valueOf(latitude) + "," + String.valueOf(longitude);
        return mNewsService.getLocation(Urls.INTERFACE_LOCATION,
                LocationCityEntity.FORMAT_OUTPUT,
                LocationCityEntity.REFERER, location).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 天气信息
     */
    public Observable<List<WeatherEntity>> getWeather(String cityName) {
        /**
         * cityName这里无需进行URLEncoder，因为Retrofit会将其进行URLEncoder。
         * %25E6%259D%25AD%25E5%25B7%259E这个是杭州encode之后的值%E6%9D%AD%E5%B7%9E再进行encode形成的
         * http://wthrcdn.etouch.cn/weather_mini?city=%25E6%259D%25AD%25E5%25B7%259E
         *
         */
        /*try {
            cityName = URLEncoder.encode(cityName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        return mNewsService.getWeather(Urls.WEATHER, cityName).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 获取ID类型
     * @param type
     * @return
     */
    private String[] getID(int type) {
        String defaultArticle = Urls.TYPE_TOP_ARTICLE;
        String id = Urls.TOP_ID;
        switch (type) {
            case AllNewsFragment.NEWS_TYPE_TOP:
                defaultArticle = Urls.TYPE_TOP_ARTICLE;
                id = Urls.TOP_ID;
                break;
            case AllNewsFragment.NEWS_TYPE_NBA:
                defaultArticle = Urls.TYPE_COMMON_ARTICLE;
                id = Urls.NBA_ID;
                break;
            case AllNewsFragment.NEWS_TYPE_CARS:
                defaultArticle = Urls.TYPE_COMMON_ARTICLE;
                id = Urls.CAR_ID;
                break;
            case AllNewsFragment.NEWS_TYPE_JOKES:
                defaultArticle = Urls.TYPE_COMMON_ARTICLE;
                id = Urls.JOKE_ID;
                break;
            default:
                defaultArticle = Urls.TYPE_TOP_ARTICLE;
                id = Urls.TOP_ID;
                break;
        }
        return new String[] {defaultArticle, id};
    }

}