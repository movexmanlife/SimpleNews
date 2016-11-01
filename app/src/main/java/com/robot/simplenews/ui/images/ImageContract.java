package com.robot.simplenews.ui.images;

import com.robot.simplenews.entity.ImageEntity;
import com.robot.simplenews.ui.base.BasePresenter;
import com.robot.simplenews.ui.base.BaseView;

import java.util.List;

/**
 */
public class ImageContract {
    public interface View extends BaseView {
        void addImages(List<ImageEntity> list);
        void showProgress();
        void hideProgress();
        void showLoadFailMsg();
    }

    public interface Presenter extends BasePresenter<View> {
        void loadImageList();
    }
}
