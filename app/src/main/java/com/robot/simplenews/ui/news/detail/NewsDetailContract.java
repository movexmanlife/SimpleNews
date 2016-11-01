package com.robot.simplenews.ui.news.detail;

import com.robot.simplenews.ui.base.BasePresenter;
import com.robot.simplenews.ui.base.BaseView;

/**
 */
public class NewsDetailContract {
    public interface View extends BaseView {
        void showNewsDetialContent(String newsDetailContent) ;
        void showProgress();
        void hideProgress();
    }

    public interface Presenter extends BasePresenter<View> {
        void loadNewsDetail(String docId);
    }
}
