package com.robot.simplenews.ui.news;

import android.content.Context;
import android.support.annotation.NonNull;

import com.robot.simplenews.api.news.NewsApi;
import com.robot.simplenews.db.NewsOperator;
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
    public void loadNews(final int type, final int pageIndex) {
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
                        addType(newsEntityList, type);

                        saveFirstPageData(newsEntityList, pageIndex, type);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mNewsView.hideSwipeRefresh();
                        mNewsView.showLoadFailMsg();

                        restoreFirstPageData(pageIndex, type);
                    }
                });
    }

    /**
     * 人工手动加上type，在数据库里面“头条”、“NBA”、“汽车”、“笑话”的Model相同，在同一张表，需要个type进行区分
     * @param newsEntityList
     * @param type
     */
    private void addType(List<NewsEntity> newsEntityList, int type) {
        if (newsEntityList != null && newsEntityList.size() > 0) {
            for (int i = 0; i < newsEntityList.size(); i++) {
                newsEntityList.get(i).setType(type);
            }
        }
    }

    /**
     * 缓存第一页的数据（始终是最新的数据）
     * @param newsEntityList
     * @param pageIndex
     */
    private void saveFirstPageData(List<NewsEntity> newsEntityList, int pageIndex, int type) {
        if (newsEntityList != null && newsEntityList.size() > 0) {
            if (pageIndex == 0) {
                new NewsOperator().deleteAll(NewsEntity.class, " type = " + String.valueOf(type));
                new NewsOperator().addAll(newsEntityList);
            }
        }
    }

    /**
     * 恢复首页的数据
     * @param pageIndex
     */
    private void restoreFirstPageData(int pageIndex, int type) {
        if (pageIndex != 0 || mNewsView.getNewsCount() > 0) {
            return;
        }

        List<NewsEntity> newsEntityList = new NewsOperator().getAllOrderByAsc(NewsEntity.class, " type = " + String.valueOf(type));
        if (newsEntityList != null && newsEntityList.size() > 0) {
            mNewsView.addNews(newsEntityList);
        }
    }
}
