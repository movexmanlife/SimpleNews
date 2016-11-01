package com.robot.simplenews.ui.about;

import com.robot.simplenews.ui.base.BasePresenter;
import com.robot.simplenews.ui.base.BaseView;

/**
 */
public class AboutContract {
    public interface View extends BaseView {
        void showVersion(String version);
    }

    public interface Presenter extends BasePresenter<View> {
        void getVersion();
    }
}
