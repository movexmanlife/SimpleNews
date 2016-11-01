package com.robot.simplenews.ui.images;

import android.content.Context;
import android.support.annotation.NonNull;

import com.robot.simplenews.api.news.NewsApi;
import com.robot.simplenews.entity.ImageEntity;
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
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mImageView.hideProgress();
                        mImageView.showLoadFailMsg();
                    }
                });
    }
}
