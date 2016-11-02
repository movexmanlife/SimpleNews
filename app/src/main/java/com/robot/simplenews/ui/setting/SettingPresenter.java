package com.robot.simplenews.ui.setting;

import android.content.Context;
import android.support.annotation.NonNull;

import com.robot.simplenews.util.RxUtil;
import com.robot.simplenews.util.VersionUtil;

import rx.Subscription;

/**
 */
public class SettingPresenter implements SettingContract.Presenter {
    private Context mContext;
    private SettingContract.View mSettingView;
    private Subscription mSubscription;

    public SettingPresenter(Context context) {
        this.mContext = context;
    }

    @Override
    public void attachView(@NonNull SettingContract.View view) {
        mSettingView = view;
    }

    @Override
    public void detachView() {
        RxUtil.unsubscribe(mSubscription);
        mSettingView = null;
    }

}
