package com.robot.simplenews.ui.news;

import android.content.Context;
import android.support.annotation.NonNull;

import com.robot.simplenews.api.news.NewsApi;
import com.robot.simplenews.entity.NewsEntity;
import com.robot.simplenews.util.RxUtil;

import java.util.List;

import rx.Subscription;
import rx.functions.Action1;

/**
 */
public class NewsPresenter implements NewsContract.Presenter {
    private Context mContext;
    private NewsContract.View mNewsView;
    private Subscription mSubscription;

    public NewsPresenter(Context context) {
        this.mContext = context;
    }

    @Override
    public void attachView(@NonNull NewsContract.View view) {
        mNewsView = view;
    }

    @Override
    public void detachView() {
        RxUtil.unsubscribe(mSubscription);
        mNewsView = null;
    }

    @Override
    public void loadNews(final int type, int pageIndex) {
        //只有第一页的或者刷新的时候才显示刷新进度条
        if (pageIndex == 0) {
            mNewsView.showSwipeRefresh();
        }
        mSubscription = NewsApi.getInstance().getNewsList(type, pageIndex)
                .subscribe(new Action1<List<NewsEntity>>() {
                    @Override
                    public void call(List<NewsEntity> newsEntityList) {
                        mNewsView.hideSwipeRefresh();
                        mNewsView.addNews(newsEntityList);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mNewsView.hideSwipeRefresh();
                        mNewsView.showLoadFailMsg();
                    }
                });
    }
}
