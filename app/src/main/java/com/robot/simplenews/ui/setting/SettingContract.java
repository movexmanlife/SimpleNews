package com.robot.simplenews.ui.setting;

import com.robot.simplenews.ui.base.BasePresenter;
import com.robot.simplenews.ui.base.BaseView;

/**
 */
public class SettingContract {
    public interface View extends BaseView {
        void showNight(boolean isNight);
    }

    public interface Presenter extends BasePresenter<View> {
    }
}
