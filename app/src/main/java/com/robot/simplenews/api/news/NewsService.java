package com.robot.simplenews.api.news;

import com.robot.simplenews.commons.Urls;
import com.robot.simplenews.entity.ImageEntity;
import com.robot.simplenews.entity.LocationCityEntity;
import com.robot.simplenews.entity.NewsDetailEntity;
import com.robot.simplenews.entity.NewsEntity;
import com.robot.simplenews.entity.WeatherEntity;
import com.robot.simplenews.http.TypeString;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface NewsService {

    // http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html
    // http://c.m.163.com/nc/article/headline/T1348647909107/20-20.html
    // http://c.m.163.com/nc/article/headline/T1348647909107/40-20.html
    // http://c.m.163.com/nc/article/headline/T1348647909107/60-20.html
    // http://c.m.163.com/nc/article/headline/T1348647909107/80-20.html
    // http://c.m.163.com/nc/article/headline/T1348647909107/100-20.html

    // http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html
    // http://c.m.163.com/nc/article/list/T1348649145984/0-20.html
    @GET("nc/article/{type}/{typeIndex}/{pageIndex}-20.html")
    Observable<List<NewsEntity>> getNewsList(@Path("type") String type, @Path("typeIndex") String typeIndex, @Path("pageIndex") int pageIndex);

    @TypeString
    @GET("nc/article/headline/T1348647909107/0-20.html")
    Observable<String> getNewsList2();

    // http://c.m.163.com/nc/article/C4N6S5S100238087/full.html
    @GET(Urls.NEW_DETAIL + "{docId}" + Urls.END_DETAIL_URL)
    Observable<NewsDetailEntity> getNewsDetail(@Path("docId") String docId);

    /**
     * 由于图片的BaseUrl跟新闻的BaseUrl不同，解决办法：
     * （1）使用@Url写上全路径；
     * （2）动态改变BaseUrl；
     * http://stackoverflow.com/questions/36498131/set-dynamic-base-url-using-retrofit-2-0-and-dagger-2
     * @param url
     * @return
     */
    @GET
    Observable<List<ImageEntity>> getImageList(@Url String url);

    // ?output=json&referer=32D45CBEEC107315C553AD1131915D366EEF79B4&location=30.281618,120.116257
    @GET
    Observable<LocationCityEntity> getLocation(@Url String url, @Query("output") String output,
                                               @Query("referer") String referer, @Query("location") String location);

    @GET
    Observable<List<WeatherEntity>> getWeather(@Url String url, @Query("city") String cityName);
}