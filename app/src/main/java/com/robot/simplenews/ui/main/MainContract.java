package com.robot.simplenews.ui.main;

import com.robot.simplenews.ui.base.BasePresenter;
import com.robot.simplenews.ui.base.BaseView;

/**
 */
public class MainContract {
    public interface View extends BaseView {
        void switch2News(int id);
        void switch2Images(int id);
        void switch2Weather(int id);
        void switch2Setting(int id);
        void switch2About(int id);
    }

    public interface Presenter extends BasePresenter<View> {
        void switchNavigation(int id);
    }
}
