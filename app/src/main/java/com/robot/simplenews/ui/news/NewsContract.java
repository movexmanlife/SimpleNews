package com.robot.simplenews.ui.news;

import com.robot.simplenews.entity.NewsEntity;
import com.robot.simplenews.ui.base.BasePresenter;
import com.robot.simplenews.ui.base.BaseView;

import java.util.List;

/**
 */
public class NewsContract {
    public interface View extends BaseView {
        void showSwipeRefresh();

        void hideSwipeRefresh();

        void addNews(List<NewsEntity> newsList);
        int getNewsCount();

        void showLoadFailMsg();
    }

    public interface Presenter extends BasePresenter<View> {
        void loadNews(int type, int pageIndex);
    }
}
