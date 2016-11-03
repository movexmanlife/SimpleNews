package com.robot.simplenews.ui.main;

import android.content.Context;
import android.support.annotation.NonNull;

import com.robot.simplenews.R;
import com.robot.simplenews.util.RxUtil;

import rx.Subscription;

/**
 */
public class MainPresenter implements MainContract.Presenter {
    private Context mContext;
    private MainContract.View mMainView;
    private Subscription mSubscription;

    public MainPresenter(Context context) {
        this.mContext = context;
    }

    @Override
    public void attachView(@NonNull MainContract.View view) {
        mMainView = view;
    }

    @Override
    public void detachView() {
        RxUtil.unsubscribe(mSubscription);
        mMainView = null;
    }

    @Override
    public void switchNavigation(int id) {
        switch (id) {
            case R.id.navigation_item_news:
                mMainView.switch2News(id);
                break;
            case R.id.navigation_item_images:
                mMainView.switch2Images(id);
                break;
            case R.id.navigation_item_weather:
                mMainView.switch2Weather(id);
                break;
            case R.id.navigation_item_setting:
                mMainView.switch2Setting(id);
                break;
            case R.id.navigation_item_about:
                mMainView.switch2About(id);
                break;
            default:
                mMainView.switch2News(id);
                break;
        }
    }
}
