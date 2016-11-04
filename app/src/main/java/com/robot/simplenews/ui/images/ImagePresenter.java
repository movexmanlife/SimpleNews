package com.robot.simplenews.ui.images;

import android.content.Context;
import android.support.annotation.NonNull;

import com.robot.simplenews.api.news.NewsApi;
import com.robot.simplenews.db.ImageOperator;
import com.robot.simplenews.db.NewsOperator;
import com.robot.simplenews.entity.ImageEntity;
import com.robot.simplenews.entity.NewsEntity;
import com.robot.simplenews.util.RxUtil;

import java.util.List;

import rx.Subscription;
import rx.functions.Action1;

/**
 */
public class ImagePresenter implements ImageContract.Presenter {
    private Context mContext;
    private ImageContract.View mImageView;
    private Subscription mSubscription;

    public ImagePresenter(Context context) {
        this.mContext = context;
    }

    @Override
    public void attachView(@NonNull ImageContract.View view) {
        mImageView = view;
    }

    @Override
    public void detachView() {
        RxUtil.unsubscribe(mSubscription);
        mImageView = null;
    }

    @Override
    public void loadImageList() {
        mImageView.showProgress();
        mSubscription = NewsApi.getInstance().getImageList()
                .subscribe(new Action1<List<ImageEntity>>() {
                    @Override
                    public void call(List<ImageEntity> imageEntityList) {
                        mImageView.addImages(imageEntityList);
                        mImageView.hideProgress();

                        saveFirstPageData(imageEntityList, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mImageView.hideProgress();
                        mImageView.showLoadFailMsg();
                        restoreFirstPageData(0);
                    }
                });
    }

    /**
     * 缓存第一页的数据（始终是最新的数据）
     * @param imageEntityList
     * @param pageIndex
     */
    private void saveFirstPageData(List<ImageEntity> imageEntityList, int pageIndex) {
        if (imageEntityList != null && imageEntityList.size() > 0) {
            if (pageIndex == 0) {
                new ImageOperator().deleteAll(ImageEntity.class);
                new ImageOperator().addAll(imageEntityList);
            }
        }
    }

    /**
     * 恢复首页的数据
     * @param pageIndex
     */
    private void restoreFirstPageData(int pageIndex) {
        if (pageIndex != 0 || mImageView.getImageCount() > 0) {
            return;
        }

        List<ImageEntity> imageEntityList = new ImageOperator().getAllOrderByDesc(ImageEntity.class);
        if (imageEntityList != null && imageEntityList.size() > 0) {
            mImageView.addImages(imageEntityList);
        }
    }
}
