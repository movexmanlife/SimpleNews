package com.robot.simplenews.ui.about;

import android.content.Context;
import android.support.annotation.NonNull;

import com.robot.simplenews.util.RxUtil;
import com.robot.simplenews.util.VersionUtil;

import rx.Subscription;

/**
 */
public class AboutPresenter implements AboutContract.Presenter {
    private Context mContext;
    private AboutContract.View mAboutView;

    public AboutPresenter(Context context) {
        this.mContext = context;
    }

    @Override
    public void attachView(@NonNull AboutContract.View view) {
        mAboutView = view;
    }

    @Override
    public void detachView() {
        mAboutView = null;
    }

    @Override
    public void getVersion() {
        String version = VersionUtil.getVersionName(mContext);
        mAboutView.showVersion(version);
    }
}
