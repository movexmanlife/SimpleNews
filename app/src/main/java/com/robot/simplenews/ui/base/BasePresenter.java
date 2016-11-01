package com.robot.simplenews.ui.base;

import android.support.annotation.NonNull;

/**
 */
public interface BasePresenter<T extends BaseView> {
    /**
     * Activity中在onCreate()中调用
     * Fragment中在onCreateView()中调用
     * @param view
     */
    void attachView(@NonNull T view);
    /**
     * Activity中在onDestory()中调用
     * Fragment中在onDestoryView()中调用
     */
    void detachView();
}