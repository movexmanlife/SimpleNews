package com.robot.simplenews.ui.main;

import com.robot.simplenews.ui.base.BasePresenter;
import com.robot.simplenews.ui.base.BaseView;

/**
 */
public class MainContract {
    public interface View extends BaseView {
        void switch2News();
        void switch2Images();
        void switch2Weather();
        void switch2About();
    }

    public interface Presenter extends BasePresenter<View> {
        void switchNavigation(int id);
    }
}
